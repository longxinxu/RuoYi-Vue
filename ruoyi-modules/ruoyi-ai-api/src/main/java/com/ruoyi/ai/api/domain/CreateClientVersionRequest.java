package com.ruoyi.ai.api.domain;

/**
 * Request for creating AI client version.
 */
public class CreateClientVersionRequest
{
    private String version;
    private String status;
    private String modelName;
    private String promptTemplate;
    private String outputSchema;
    private String advisorCodes;
    private Integer tokenBudget;
    private Integer toolBudget;

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


    public String getAdvisorCodes()
    {
        return advisorCodes;
    }

    public void setAdvisorCodes(String advisorCodes)
    {
        this.advisorCodes = advisorCodes;
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

    public String getOutputSchema()
    {
        return outputSchema;
    }

    public void setOutputSchema(String outputSchema)
    {
        this.outputSchema = outputSchema;
    }
}
