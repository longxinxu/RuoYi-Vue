package com.ruoyi.ai.api.domain;

import java.util.Map;

/**
 * Request for AI client debug run.
 */
public class ClientDebugRunRequest
{
    private String appId;
    private String version;
    private Map<String, Object> variables;

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Map<String, Object> getVariables()
    {
        return variables;
    }

    public void setVariables(Map<String, Object> variables)
    {
        this.variables = variables;
    }
}
