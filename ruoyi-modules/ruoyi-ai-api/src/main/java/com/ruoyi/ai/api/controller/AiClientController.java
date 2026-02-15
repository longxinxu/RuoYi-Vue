package com.ruoyi.ai.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.ai.api.domain.ClientDebugRunRequest;
import com.ruoyi.ai.api.domain.CreateClientRequest;
import com.ruoyi.ai.api.domain.CreateClientVersionRequest;
import com.ruoyi.ai.client.ClientDefinitionService;
import com.ruoyi.ai.client.ClientEngine;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * AI client management and debug API.
 */
@RestController
@RequestMapping("/ai/client")
public class AiClientController
{
    private final ClientDefinitionService clientDefinitionService;
    private final ClientEngine clientEngine;

    public AiClientController(ClientDefinitionService clientDefinitionService, ClientEngine clientEngine)
    {
        this.clientDefinitionService = clientDefinitionService;
        this.clientEngine = clientEngine;
    }

    @PostMapping
    public AjaxResult createClient(@RequestBody CreateClientRequest request)
    {
        AiClientDefinition definition = new AiClientDefinition();
        definition.setAppId(request.getAppId());
        definition.setClientCode(request.getClientCode());
        definition.setClientName(request.getClientName());
        definition.setDescription(request.getDescription());
        return AjaxResult.success(clientDefinitionService.createClient(definition));
    }

    @PostMapping("/{id}/version")
    public AjaxResult createClientVersion(@PathVariable("id") Long clientId,
        @RequestBody CreateClientVersionRequest request)
    {
        AiClientVersion version = new AiClientVersion();
        version.setVersion(request.getVersion());
        version.setStatus(request.getStatus());
        version.setModelName(request.getModelName());
        version.setPromptTemplate(request.getPromptTemplate());
        version.setOutputSchema(request.getOutputSchema());
        return AjaxResult.success(clientDefinitionService.createClientVersion(clientId, version));
    }

    @PostMapping("/{id}/debug-run")
    public AjaxResult debugRun(@PathVariable("id") Long clientId, @RequestBody ClientDebugRunRequest request)
    {
        String output = clientEngine.debugRun(request.getAppId(), clientId, request.getVersion(), request.getVariables());
        return AjaxResult.success().put("output", output);
    }
}
