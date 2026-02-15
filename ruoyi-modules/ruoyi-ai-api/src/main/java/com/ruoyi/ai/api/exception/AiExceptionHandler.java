package com.ruoyi.ai.api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ruoyi.ai.runtime.AiError;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;
import java.util.UUID;
import com.ruoyi.ai.runtime.ExecutionContextHolder;
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
        String traceId = resolveTraceId();
        AiError error = new AiError(traceId, e.getErrorCode(), e.getMessage());
        return AjaxResult.error(error.getMessage()).put("error", error);
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e)
    {
        String traceId = resolveTraceId();
        AiError error = new AiError(traceId, AiErrorCode.AI_UNEXPECTED_ERROR,
            AiErrorCode.AI_UNEXPECTED_ERROR.getDefaultMessage());
        return AjaxResult.error(error.getMessage()).put("error", error);
    }

    private String resolveTraceId()
    {
        String traceId = ExecutionContextHolder.getContext().getTraceId();
        return traceId != null ? traceId : UUID.randomUUID().toString().replace("-", "");
    }
}
