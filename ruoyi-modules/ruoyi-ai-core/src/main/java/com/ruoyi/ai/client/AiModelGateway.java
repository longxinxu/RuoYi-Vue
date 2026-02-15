package com.ruoyi.ai.client;

/**
 * AI model gateway abstraction.
 */
public interface AiModelGateway
{
    String callModel(String modelName, String prompt);
}
