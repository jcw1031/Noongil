package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.domain.program.ProgramsCountByBorough;

public record ProgramsCountByBoroughResponse(String borough, int count) {

    public static ProgramsCountByBoroughResponse from(ProgramsCountByBorough programsCountByBorough) {
        return new ProgramsCountByBoroughResponse(programsCountByBorough.borough(),
                (int) programsCountByBorough.count());
    }
}
