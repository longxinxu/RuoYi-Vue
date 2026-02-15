package com.ruoyi.ai.runtime;

import java.io.Serializable;

/**
 * Standardized AI error model.
 */
public class AiError implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String traceId;
    private final String errorCode;
    private final String message;

    public AiError(String traceId, AiErrorCode errorCode)
    {
        this(traceId, errorCode.getCode(), errorCode.getDefaultMessage());
    }

    public AiError(String traceId, AiErrorCode errorCode, String message)
    {
        this(traceId, errorCode.getCode(), message);
    }

    public AiError(String traceId, String errorCode, String message)
    {
        this.traceId = traceId;
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getTraceId()
    {
        return traceId;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getMessage()
    {
        return message;
    }
}
