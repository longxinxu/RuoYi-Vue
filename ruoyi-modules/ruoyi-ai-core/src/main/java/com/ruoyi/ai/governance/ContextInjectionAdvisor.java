package com.ruoyi.ai.governance;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.runtime.ExecutionContext;

/**
 * Inject execution context metadata into variables.
 */
@Component
public class ContextInjectionAdvisor implements Advisor
{
    public static final String CODE = "context_injection";

    @Override
    public String getCode()
    {
        return CODE;
    }

    @Override
    public void preAdvisor(ExecutionContext context, AiClientVersion clientVersion, Map<String, Object> variables,
        String prompt)
    {
        if (context == null || variables == null)
        {
            return;
        }
        variables.put("traceId", context.getTraceId());
        variables.put("sessionId", context.getSessionId());
        variables.put("tenantId", context.getTenantId());
        variables.put("userId", context.getUserId());
    }
}
