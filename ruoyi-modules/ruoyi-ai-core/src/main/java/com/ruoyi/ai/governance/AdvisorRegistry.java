package com.ruoyi.ai.governance;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Advisor registry resolved by advisor code.
 */
@Component
public class AdvisorRegistry
{
    private final Map<String, Advisor> advisorMap;

    public AdvisorRegistry(List<Advisor> advisors)
    {
        Map<String, Advisor> map = new LinkedHashMap<>();
        for (Advisor advisor : advisors)
        {
            map.put(advisor.getCode(), advisor);
        }
        this.advisorMap = Collections.unmodifiableMap(map);
    }

    public Advisor getAdvisor(String code)
    {
        return advisorMap.get(code);
    }
}
