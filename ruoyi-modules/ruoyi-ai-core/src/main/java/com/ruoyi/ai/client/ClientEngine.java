package com.ruoyi.ai.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.governance.Advisor;
import com.ruoyi.ai.governance.AdvisorRegistry;
import com.ruoyi.ai.governance.BudgetGuard;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;
import com.ruoyi.ai.runtime.ExecutionContext;
import com.ruoyi.ai.runtime.ExecutionContextHolder;

/**
 * Client execution pipeline with governance hooks.
 */
@Service
public class ClientEngine
{
    private static final Logger log = LoggerFactory.getLogger(ClientEngine.class);
    private static final BigDecimal TOKEN_UNIT_PRICE = new BigDecimal("0.00001");

    private final ClientResolver clientResolver;
    private final AiModelGateway aiModelGateway;
    private final AdvisorRegistry advisorRegistry;
    private final BudgetGuard budgetGuard;

    public ClientEngine(ClientResolver clientResolver, AiModelGateway aiModelGateway, AdvisorRegistry advisorRegistry,
        BudgetGuard budgetGuard)
    {
        this.clientResolver = clientResolver;
        this.aiModelGateway = aiModelGateway;
        this.advisorRegistry = advisorRegistry;
        this.budgetGuard = budgetGuard;
    }

    public String debugRun(String appId, Long clientId, String version, Map<String, Object> variables)
    {
        long startNs = System.nanoTime();
        ExecutionContext context = ExecutionContextHolder.getContext();
        AiClientDefinition clientDefinition = clientResolver.resolveClient(appId, clientId);
        AiClientVersion clientVersion = clientResolver.resolveVersion(clientId, version);
        Map<String, Object> runtimeVariables = toRuntimeVariables(context, variables);
        String prompt = renderPrompt(clientVersion.getPromptTemplate(), runtimeVariables);
        List<Advisor> advisors = resolveAdvisors(clientVersion);
        runPreAdvisors(advisors, context, clientVersion, runtimeVariables, prompt);

        int promptTokens = estimateTokens(prompt);
        String output;
        try
        {
            output = aiModelGateway.callModel(clientVersion.getModelName(), prompt);
        }
        catch (AiException e)
        {
            logExecution(context, clientDefinition.getClientCode(), clientVersion.getVersion(), prompt, null, promptTokens, 0,
                "failed", 0L, e.getErrorCode().getCode());
            throw e;
        }
        catch (Exception e)
        {
            throw new AiException(AiErrorCode.AI_MODEL_CALL_FAILED, "Model invocation failed for client: "
                + clientDefinition.getClientCode(), e);
        }

        String parsedOutput = parseOutput(output);
        String advisedOutput = runPostAdvisors(advisors, context, clientVersion, parsedOutput);
        int outputTokens = estimateTokens(advisedOutput);

        budgetGuard.checkTokenBudget(clientVersion, promptTokens, outputTokens);
        budgetGuard.checkToolBudget(clientVersion, 0);

        long durationMs = (System.nanoTime() - startNs) / 1_000_000;
        logExecution(context, clientDefinition.getClientCode(), clientVersion.getVersion(), prompt, advisedOutput,
            promptTokens, outputTokens, "success", durationMs, null);
        return advisedOutput;
    }

    private Map<String, Object> toRuntimeVariables(ExecutionContext context, Map<String, Object> variables)
    {
        Map<String, Object> runtimeVariables = new LinkedHashMap<>();
        if (variables != null)
        {
            runtimeVariables.putAll(variables);
        }
        if (context != null)
        {
            context.putVariables(runtimeVariables);
        }
        return runtimeVariables;
    }

    private List<Advisor> resolveAdvisors(AiClientVersion clientVersion)
    {
        List<Advisor> advisors = new ArrayList<>();
        for (String advisorCode : clientVersion.advisorCodeList())
        {
            String trimmedCode = advisorCode == null ? null : advisorCode.trim();
            if (trimmedCode == null || trimmedCode.isEmpty())
            {
                continue;
            }
            Advisor advisor = advisorRegistry.getAdvisor(trimmedCode);
            if (advisor == null)
            {
                throw new AiException(AiErrorCode.AI_REQUEST_INVALID, "Unknown advisor: " + trimmedCode);
            }
            advisors.add(advisor);
        }
        return advisors;
    }

    private void runPreAdvisors(List<Advisor> advisors, ExecutionContext context, AiClientVersion clientVersion,
        Map<String, Object> variables, String prompt)
    {
        for (Advisor advisor : advisors)
        {
            advisor.preAdvisor(context, clientVersion, variables, prompt);
        }
    }

    private String runPostAdvisors(List<Advisor> advisors, ExecutionContext context, AiClientVersion clientVersion,
        String output)
    {
        String currentOutput = output;
        for (Advisor advisor : advisors)
        {
            currentOutput = advisor.postAdvisor(context, clientVersion, currentOutput);
        }
        return currentOutput;
    }

    private String renderPrompt(String promptTemplate, Map<String, Object> variables)
    {
        if (promptTemplate == null)
        {
            throw new AiException(AiErrorCode.AI_PROMPT_RENDER_FAILED, "Prompt template is required.");
        }
        String renderedPrompt = promptTemplate;
        if (variables != null)
        {
            for (Map.Entry<String, Object> entry : variables.entrySet())
            {
                String key = "${" + entry.getKey() + "}";
                renderedPrompt = renderedPrompt.replace(key, String.valueOf(entry.getValue()));
            }
        }
        return renderedPrompt;
    }

    private String parseOutput(String output)
    {
        if (output == null)
        {
            throw new AiException(AiErrorCode.AI_OUTPUT_PARSE_FAILED);
        }
        return output.trim();
    }

    private int estimateTokens(String text)
    {
        if (text == null || text.isEmpty())
        {
            return 0;
        }
        return Math.max(1, (int) Math.ceil(text.length() / 4.0));
    }

    private void logExecution(ExecutionContext context, String clientCode, String version, String prompt, String output,
        int promptTokens, int outputTokens, String status, long durationMs, String errorCode)
    {
        int totalTokens = promptTokens + outputTokens;
        BigDecimal cost = TOKEN_UNIT_PRICE.multiply(BigDecimal.valueOf(totalTokens)).setScale(6, RoundingMode.HALF_UP);
        log.info(
            "ai_client_exec traceId={} sessionId={} tenantId={} userId={} clientCode={} version={} status={} durationMs={} promptTokens={} outputTokens={} totalTokens={} cost={} inputSummary={} outputSummary={} errorCode={}",
            context.getTraceId(), context.getSessionId(), context.getTenantId(), context.getUserId(), clientCode, version,
            status, durationMs, promptTokens, outputTokens, totalTokens, cost, summarize(prompt), summarize(output),
            errorCode);
    }

    private String summarize(String value)
    {
        if (value == null)
        {
            return "-";
        }
        String normalized = value.replaceAll("\\s+", " ").trim();
        return normalized.length() <= 120 ? normalized : normalized.substring(0, 120) + "...";
    }
}
