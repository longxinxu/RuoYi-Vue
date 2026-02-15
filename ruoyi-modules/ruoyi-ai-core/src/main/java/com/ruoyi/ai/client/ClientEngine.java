package com.ruoyi.ai.client;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;

/**
 * Minimal client execution pipeline.
 */
@Service
public class ClientEngine
{
    private final ClientResolver clientResolver;
    private final AiModelGateway aiModelGateway;

    public ClientEngine(ClientResolver clientResolver, AiModelGateway aiModelGateway)
    {
        this.clientResolver = clientResolver;
        this.aiModelGateway = aiModelGateway;
    }

    public String debugRun(String appId, Long clientId, String version, Map<String, Object> variables)
    {
        AiClientDefinition clientDefinition = clientResolver.resolveClient(appId, clientId);
        AiClientVersion clientVersion = clientResolver.resolveVersion(clientId, version);
        String prompt = renderPrompt(clientVersion.getPromptTemplate(), variables);
        try
        {
            String output = aiModelGateway.callModel(clientVersion.getModelName(), prompt);
            return parseOutput(output);
        }
        catch (AiException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new AiException(AiErrorCode.AI_MODEL_CALL_FAILED, "Model invocation failed for client: "
                + clientDefinition.getClientCode(), e);
        }
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
}
