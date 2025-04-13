package com.woopaca.noongil.infrastructure.publicdata;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ProgramResponse(@JsonAlias("tbPartcptn") Programs programs) {

    public List<ProgramDto> getPrograms() {
        if (this.programs == null) {
            return Collections.emptyList();
        }

        return this.programs.row()
                .stream()
                .map(ProgramDto::new)
                .toList();
    }

    public record Programs(List<Map<String, String>> row) {
    }
}
