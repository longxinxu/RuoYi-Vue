package com.ruoyi.ai.api.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ruoyi.ai.runtime.ExecutionContext;
import com.ruoyi.ai.runtime.ExecutionContextHolder;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * Inject execution context for AI APIs.
 */
@Component
public class AiExecutionContextFilter extends OncePerRequestFilter
{
    private static final String TRACE_HEADER = "X-Trace-Id";
    private static final String SESSION_HEADER = "X-Session-Id";
    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        return !request.getRequestURI().startsWith("/ai/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        ExecutionContext context = new ExecutionContext();
        context.setTraceId(resolveOrGenerate(request.getHeader(TRACE_HEADER)));
        context.setSessionId(resolveOrGenerate(request.getHeader(SESSION_HEADER)));
        context.setTenantId(request.getHeader(TENANT_HEADER));
        context.setUserId(resolveUserId());
        ExecutionContextHolder.setContext(context);
        response.setHeader(TRACE_HEADER, context.getTraceId());
        try
        {
            filterChain.doFilter(request, response);
        }
        finally
        {
            ExecutionContextHolder.clear();
        }
    }

    private String resolveOrGenerate(String value)
    {
        if (StringUtils.hasText(value))
        {
            return value.trim();
        }
        return UUID.randomUUID().toString().replace("-", "");
    }

    private Long resolveUserId()
    {
        try
        {
            return SecurityUtils.getUserId();
        }
        catch (Exception ignored)
        {
            return null;
        }
    }
}
