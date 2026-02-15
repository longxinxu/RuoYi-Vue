package com.ruoyi.ai.infra.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.ai.client.domain.AiClientDefinition;
import com.ruoyi.ai.client.domain.AiClientVersion;

/**
 * MyBatis mapper for AI client persistence.
 */
public interface AiClientMapper
{
    int insertClient(AiClientDefinition clientDefinition);

    AiClientDefinition selectClientById(@Param("clientId") Long clientId);

    int insertClientVersion(AiClientVersion clientVersion);

    AiClientVersion selectClientVersion(@Param("clientId") Long clientId, @Param("version") String version);

    AiClientVersion selectPublishedVersion(@Param("clientId") Long clientId);

    int deprecatePublishedVersions(@Param("clientId") Long clientId);

    int updateVersionStatus(@Param("versionId") Long versionId, @Param("status") String status);
}
