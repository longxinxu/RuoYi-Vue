package com.ruoyi.ai.api.exception;

import java.util.UUID;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ruoyi.ai.runtime.AiError;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * Unified exception handling for AI APIs.
 */
@RestControllerAdvice
public class AiExceptionHandler
{
    @ExceptionHandler(AiException.class)
    public AjaxResult handleAiException(AiException e)
    {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        AiError error = new AiError(traceId, e.getErrorCode(), e.getMessage());
        return AjaxResult.error(error.getMessage()).put("error", error);
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e)
    {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        AiError error = new AiError(traceId, AiErrorCode.AI_UNEXPECTED_ERROR,
            AiErrorCode.AI_UNEXPECTED_ERROR.getDefaultMessage());
        return AjaxResult.error(error.getMessage()).put("error", error);
    }
}
