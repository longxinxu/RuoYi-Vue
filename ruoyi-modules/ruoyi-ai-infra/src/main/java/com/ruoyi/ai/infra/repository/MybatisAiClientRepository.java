package com.ruoyi.ai.infra.repository;

import org.springframework.stereotype.Repository;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.client.repository.AiClientRepository;
import com.ruoyi.ai.infra.mapper.AiClientMapper;

/**
 * MyBatis implementation for AI client repository.
 */
@Repository
public class MybatisAiClientRepository implements AiClientRepository
{
    private final AiClientMapper aiClientMapper;

    public MybatisAiClientRepository(AiClientMapper aiClientMapper)
    {
        this.aiClientMapper = aiClientMapper;
    }

    @Override
    public int insertClient(AiClientDefinition clientDefinition)
    {
        return aiClientMapper.insertClient(clientDefinition);
    }

    @Override
    public AiClientDefinition selectClientById(Long clientId)
    {
        return aiClientMapper.selectClientById(clientId);
    }

    @Override
    public int insertClientVersion(AiClientVersion clientVersion)
    {
        return aiClientMapper.insertClientVersion(clientVersion);
    }

    @Override
    public AiClientVersion selectClientVersion(Long clientId, String version)
    {
        return aiClientMapper.selectClientVersion(clientId, version);
    }

    @Override
    public AiClientVersion selectPublishedVersion(Long clientId)
    {
        return aiClientMapper.selectPublishedVersion(clientId);
    }

    @Override
    public int deprecatePublishedVersions(Long clientId)
    {
        return aiClientMapper.deprecatePublishedVersions(clientId);
    }

    @Override
    public int updateVersionStatus(Long versionId, String status)
    {
        return aiClientMapper.updateVersionStatus(versionId, status);
    }
}
