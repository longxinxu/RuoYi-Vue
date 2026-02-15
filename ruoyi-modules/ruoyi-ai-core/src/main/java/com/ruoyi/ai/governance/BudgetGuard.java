package com.ruoyi.ai.governance;

import org.springframework.stereotype.Component;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;

/**
 * Token and tool budget guard.
 */
@Component
public class BudgetGuard
{
    public void checkTokenBudget(AiClientVersion clientVersion, int promptTokens, int outputTokens)
    {
        Integer tokenBudget = clientVersion.getTokenBudget();
        if (tokenBudget == null || tokenBudget <= 0)
        {
            return;
        }
        int total = promptTokens + outputTokens;
        if (total > tokenBudget)
        {
            throw new AiException(AiErrorCode.AI_TOKEN_BUDGET_EXCEEDED,
                "Token budget exceeded. budget=" + tokenBudget + ", actual=" + total);
        }
    }

    public void checkToolBudget(AiClientVersion clientVersion, int toolCalls)
    {
        Integer toolBudget = clientVersion.getToolBudget();
        if (toolBudget == null || toolBudget <= 0)
        {
            return;
        }
        if (toolCalls > toolBudget)
        {
            throw new AiException(AiErrorCode.AI_TOOL_BUDGET_EXCEEDED,
                "Tool budget exceeded. budget=" + toolBudget + ", actual=" + toolCalls);
        }
    }
}
