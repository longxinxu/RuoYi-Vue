package com.ruoyi.ai.runtime;

/**
 * AI domain error code definitions.
 */
public enum AiErrorCode
{
    AI_CLIENT_NOT_FOUND("AI-CLIENT-404", "AI Client definition not found."),
    AI_CLIENT_VERSION_NOT_FOUND("AI-CLIENT-VERSION-404", "AI Client version not found."),
    AI_PROMPT_RENDER_FAILED("AI-PROMPT-500", "Prompt render failed."),
    AI_MODEL_CALL_FAILED("AI-MODEL-500", "Model invocation failed."),
    AI_OUTPUT_PARSE_FAILED("AI-OUTPUT-500", "Model output parse failed."),
    AI_REQUEST_INVALID("AI-REQUEST-400", "AI request parameters are invalid."),
    AI_UNEXPECTED_ERROR("AI-UNEXPECTED-500", "AI orchestration unexpected error.");

    private final String code;
    private final String defaultMessage;

    AiErrorCode(String code, String defaultMessage)
    {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode()
    {
        return code;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }
}
