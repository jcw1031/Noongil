package com.woopaca.noongil.infrastructure.publicdata;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ProgramResponse(@JsonAlias("tbPartcptn") Programs programs) {

    public static ProgramResponse empty() {
        return new ProgramResponse(new Programs(Collections.emptyList(), 0));
    }

    public List<ProgramDto> getPrograms() {
        if (this.programs == null) {
            return Collections.emptyList();
        }

        return this.programs.row()
                .stream()
                .map(ProgramDto::new)
                .toList();
    }

    public int getTotalCount() {
        if (this.programs == null) {
            return 0;
        }
        return this.programs.totalCount();
    }

    public record Programs(List<Map<String, String>> row, @JsonAlias("list_total_count") int totalCount) {
    }
}
