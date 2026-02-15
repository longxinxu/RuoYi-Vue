package com.ruoyi.ai.client.repository;

import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;

/**
 * Repository abstraction for AI client data.
 */
public interface AiClientRepository
{
    int insertClient(AiClientDefinition clientDefinition);

    AiClientDefinition selectClientById(Long clientId);

    int insertClientVersion(AiClientVersion clientVersion);

    AiClientVersion selectClientVersion(Long clientId, String version);

    AiClientVersion selectPublishedVersion(Long clientId);

    int deprecatePublishedVersions(Long clientId);

    int updateVersionStatus(Long versionId, String status);
}
