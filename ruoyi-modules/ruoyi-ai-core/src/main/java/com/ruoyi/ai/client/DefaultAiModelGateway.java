package com.ruoyi.ai.client;

import org.springframework.stereotype.Component;

/**
 * Default model gateway for MVP debug runs.
 */
@Component
public class DefaultAiModelGateway implements AiModelGateway
{
    @Override
    public String callModel(String modelName, String prompt)
    {
        return "[" + modelName + "] " + prompt;
    }
}
