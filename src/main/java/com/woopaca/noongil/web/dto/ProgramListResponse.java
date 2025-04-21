package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.domain.program.Program;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ProgramListResponse(Long id, String name, String receptionStatus, String feeType, List<Integer> ageRange,
                                  LocalDate receptionStartDate, LocalDate receptionEndDate) {

    public static ProgramListResponse from(Program program) {
        return ProgramListResponse.builder()
                .id(program.getId())
                .name(program.getName())
                .receptionStatus(determineReceptionStatus(program.getReceptionStartDate(), program.getReceptionEndDate()))
                .feeType(program.getFeeType().getExpression())
                .ageRange(program.getAgeRange())
                .receptionStartDate(program.getReceptionStartDate())
                .receptionEndDate(program.getReceptionEndDate())
                .build();
    }

    private static String determineReceptionStatus(LocalDate receptionStartDate, LocalDate receptionEndDate) {
        LocalDate now = LocalDate.now();

        if (receptionStartDate != null && !receptionStartDate.isAfter(now)
            && receptionEndDate != null && !receptionEndDate.isBefore(now)) {
            return "모집 중";
        }
        if (receptionStartDate != null && receptionStartDate.isAfter(now)) {
            return "모집 예정";
        }
        return "모집 종료";
    }
}
