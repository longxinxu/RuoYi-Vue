package com.ruoyi.ai.client;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.client.model.AiVersionStatus;
import com.ruoyi.ai.client.repository.AiClientRepository;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;

/**
 * AI client definition lifecycle service.
 */
@Service
public class ClientDefinitionService
{
    private final AiClientRepository aiClientRepository;

    public ClientDefinitionService(AiClientRepository aiClientRepository)
    {
        this.aiClientRepository = aiClientRepository;
    }

    public AiClientDefinition createClient(AiClientDefinition clientDefinition)
    {
        int rows = aiClientRepository.insertClient(clientDefinition);
        if (rows <= 0)
        {
            throw new AiException(AiErrorCode.AI_REQUEST_INVALID, "Failed to create AI client definition.");
        }
        return clientDefinition;
    }

    @Transactional(rollbackFor = Exception.class)
    public AiClientVersion createClientVersion(Long clientId, AiClientVersion clientVersion)
    {
        clientVersion.setClientId(clientId);
        if (clientVersion.getStatus() == null)
        {
            clientVersion.setStatus(AiVersionStatus.DRAFT);
        }
        int rows = aiClientRepository.insertClientVersion(clientVersion);
        if (rows <= 0)
        {
            throw new AiException(AiErrorCode.AI_REQUEST_INVALID, "Failed to create AI client version.");
        }
        if (AiVersionStatus.PUBLISHED.equals(clientVersion.getStatus()))
        {
            aiClientRepository.deprecatePublishedVersions(clientId);
            aiClientRepository.updateVersionStatus(clientVersion.getVersionId(), AiVersionStatus.PUBLISHED);
        }
        return clientVersion;
    }
}
