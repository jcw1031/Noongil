package com.woopaca.noongil.application.program;

import com.woopaca.noongil.adapter.program.ProgramCache;
import com.woopaca.noongil.domain.program.ProgramRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

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
        programRepository.findAll()
                .forEach(program -> {
                    String uniqueId = program.getUniqueId();
                    programCache.setIfAbsent(uniqueId);
                });
    }
}
