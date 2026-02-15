package com.ruoyi.ai.governance;

import org.springframework.stereotype.Component;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;
import com.ruoyi.ai.runtime.ExecutionContext;

/**
 * Validate output with lightweight schema checks.
 */
@Component
public class OutputSchemaValidatorAdvisor implements Advisor
{
    public static final String CODE = "output_schema_validator";

    @Override
    public String getCode()
    {
        return CODE;
    }

    @Override
    public String postAdvisor(ExecutionContext context, AiClientVersion clientVersion, String output)
    {
        String schema = clientVersion.getOutputSchema();
        if (schema == null || schema.trim().isEmpty() || output == null)
        {
            return output;
        }
        String normalizedSchema = schema.trim().toLowerCase();
        if ("json".equals(normalizedSchema))
        {
            String trimmed = output.trim();
            if (!(trimmed.startsWith("{") || trimmed.startsWith("[")))
            {
                throw new AiException(AiErrorCode.AI_ADVISOR_VALIDATION_FAILED, "Output is not a valid JSON shape.");
            }
        }
        return output;
    }
}
