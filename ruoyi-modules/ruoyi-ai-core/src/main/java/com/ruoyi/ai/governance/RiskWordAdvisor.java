package com.ruoyi.ai.governance;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import com.ruoyi.ai.client.domain.AiClientVersion;
import com.ruoyi.ai.runtime.AiErrorCode;
import com.ruoyi.ai.runtime.AiException;
import com.ruoyi.ai.runtime.ExecutionContext;

/**
 * Basic risk word detector for model output.
 */
@Component
public class RiskWordAdvisor implements Advisor
{
    public static final String CODE = "risk_word_detector";

    private static final List<String> BLOCKED_WORDS = Arrays.asList("炸弹", "枪支", "赌博", "毒品", "恐怖");

    @Override
    public String getCode()
    {
        return CODE;
    }

    @Override
    public String postAdvisor(ExecutionContext context, AiClientVersion clientVersion, String output)
    {
        if (output == null)
        {
            return null;
        }
        for (String blockedWord : BLOCKED_WORDS)
        {
            if (output.contains(blockedWord))
            {
                throw new AiException(AiErrorCode.AI_RISK_WORD_DETECTED,
                    "Model output contains blocked content: " + blockedWord);
            }
        }
        return output;
    }
}
