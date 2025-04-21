package com.woopaca.noongil.application.program;

import com.woopaca.noongil.adapter.program.ProgramCache;
import com.woopaca.noongil.domain.program.ProgramRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProgramCacheInitializer {

    private final ProgramRepository programRepository;
    private final ProgramCache programCache;

    public ProgramCacheInitializer(ProgramRepository programRepository, ProgramCache programCache) {
        this.programRepository = programRepository;
        this.programCache = programCache;
    }

    @PostConstruct
    private void initializeCache() {
        try {
            programRepository.findAll()
                    .forEach(program -> {
                        String uniqueId = program.getUniqueId();
                        programCache.setIfAbsent(uniqueId);
                    });
        } catch (RedisConnectionFailureException e) {
            log.warn("Redis 연결 실패 - 캐시 초기화 생략", e);
        }
    }
}
