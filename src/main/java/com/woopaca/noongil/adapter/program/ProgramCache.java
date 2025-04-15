package com.woopaca.noongil.adapter.program;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProgramCache {

    private static final String CACHE_KEY_PREFIX = "program_cache";

    private final RedisTemplate<String, String> redisTemplate;

    public ProgramCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean setIfAbsent(String uniqueId) {
        if (!StringUtils.hasText(uniqueId)) {
            return false;
        }

        String key = String.join(":", CACHE_KEY_PREFIX, uniqueId);
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, "1");
        return result != null && result;
    }

    public void delete(String uniqueId) {
        if (!StringUtils.hasText(uniqueId)) {
            return;
        }

        String key = String.join(":", CACHE_KEY_PREFIX, uniqueId);
        redisTemplate.delete(key);
    }
}
