package com.ruoyi.ai.client;

import org.springframework.stereotype.Service;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.client.repository.AiClientRepository;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;

/**
 * Resolve AI client definitions and versions.
 */
@Service
public class ClientResolver
{
    private final AiClientRepository aiClientRepository;

    public ClientResolver(AiClientRepository aiClientRepository)
    {
        this.aiClientRepository = aiClientRepository;
    }

    public AiClientDefinition resolveClient(String appId, Long clientId)
    {
        AiClientDefinition definition = aiClientRepository.selectClientById(clientId);
        if (definition == null)
        {
            throw new AiException(AiErrorCode.AI_CLIENT_NOT_FOUND);
        }
        if (appId != null && !appId.equals(definition.getAppId()))
        {
            throw new AiException(AiErrorCode.AI_CLIENT_NOT_FOUND, "AI client not found under current appId.");
        }
        return definition;
    }

    public AiClientVersion resolveVersion(Long clientId, String version)
    {
        AiClientVersion resolvedVersion = version == null
            ? aiClientRepository.selectPublishedVersion(clientId)
            : aiClientRepository.selectClientVersion(clientId, version);
        if (resolvedVersion == null)
        {
            throw new AiException(AiErrorCode.AI_CLIENT_VERSION_NOT_FOUND);
        }
        return resolvedVersion;
    }
}
