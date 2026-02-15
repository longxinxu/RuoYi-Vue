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
}
