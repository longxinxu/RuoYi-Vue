package com.ruoyi.ai.client.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * AI client version definition.
 */
public class AiClientVersion implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long versionId;
    private Long clientId;
    private String version;
    private String status;
    private String modelName;
    private String promptTemplate;
    private String outputSchema;
    private String advisorCodes;
    private Integer tokenBudget;
    private Integer toolBudget;
    private Date createTime;
    private Date updateTime;

    public Long getVersionId()
    {
        return versionId;
    }

    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getModelName()
    {
        return modelName;
    }

    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getPromptTemplate()
    {
        return promptTemplate;
    }

    public void setPromptTemplate(String promptTemplate)
    {
        this.promptTemplate = promptTemplate;
    }

    public String getOutputSchema()
    {
        return outputSchema;
    }

    public void setOutputSchema(String outputSchema)
    {
        this.outputSchema = outputSchema;
    }


    public String getAdvisorCodes()
    {
        return advisorCodes;
    }

    public void setAdvisorCodes(String advisorCodes)
    {
        this.advisorCodes = advisorCodes;
    }

    public List<String> advisorCodeList()
    {
        if (advisorCodes == null || advisorCodes.trim().isEmpty())
        {
            return Collections.emptyList();
        }
        return Arrays.asList(advisorCodes.split(","));
    }

    public Integer getTokenBudget()
    {
        return tokenBudget;
    }

    public void setTokenBudget(Integer tokenBudget)
    {
        this.tokenBudget = tokenBudget;
    }

    public Integer getToolBudget()
    {
        return toolBudget;
    }

    public void setToolBudget(Integer toolBudget)
    {
        this.toolBudget = toolBudget;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
