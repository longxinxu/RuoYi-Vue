package com.ruoyi.ai.governance;

import java.util.Map;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.runtime.ExecutionContext;

/**
 * Advisor SPI for client execution governance hooks.
 */
public interface Advisor
{
    String getCode();

    default void preAdvisor(ExecutionContext context, AiClientVersion clientVersion, Map<String, Object> variables,
        String prompt)
    {
    }

    default String postAdvisor(ExecutionContext context, AiClientVersion clientVersion, String output)
    {
        return output;
    }
}
