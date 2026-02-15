package com.ruoyi.ai.runtime;

/**
 * AI runtime exception with standardized error code.
 */
public class AiException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final AiErrorCode errorCode;

    public AiException(AiErrorCode errorCode)
    {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public AiException(AiErrorCode errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public AiException(AiErrorCode errorCode, String message, Throwable cause)
    {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AiErrorCode getErrorCode()
    {
        return errorCode;
    }
}
