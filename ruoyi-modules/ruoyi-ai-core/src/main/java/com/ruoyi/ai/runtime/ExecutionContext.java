package com.ruoyi.ai.runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Request-scoped execution context for AI runtime.
 */
public class ExecutionContext
{
    private String traceId;
    private String sessionId;
    private String tenantId;
    private Long userId;
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, Object> artifacts = new HashMap<>();

    public static ExecutionContext empty()
    {
        return new ExecutionContext();
    }

    public String getTraceId()
    {
        return traceId;
    }

    public void setTraceId(String traceId)
    {
        this.traceId = traceId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Map<String, Object> getVariables()
    {
        return Collections.unmodifiableMap(variables);
    }

    public void putVariable(String key, Object value)
    {
        this.variables.put(key, value);
    }

    public void putVariables(Map<String, Object> data)
    {
        if (data != null)
        {
            this.variables.putAll(data);
        }
    }

    public Map<String, Object> getArtifacts()
    {
        return Collections.unmodifiableMap(artifacts);
    }

    public void putArtifact(String key, Object value)
    {
        this.artifacts.put(key, value);
    }
}
