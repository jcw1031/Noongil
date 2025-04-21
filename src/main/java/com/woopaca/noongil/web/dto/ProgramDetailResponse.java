package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.domain.address.Coordinate;
import com.woopaca.noongil.domain.program.Program;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ProgramDetailResponse(Long id, String name, String feeType, String feeAmount, List<Integer> ageRange,
                                    LocalDate receptionStartDate, LocalDate receptionEndDate,
                                    LocalDate programStartDate, LocalDate programEndDate, String institution,
                                    String address, Coordinate coordinate, String gender, String receptionMethod,
                                    String receptionUrl) {

    public static ProgramDetailResponse from(Program program) {
        return ProgramDetailResponse.builder()
                .id(program.getId())
                .name(program.getName())
                .feeType(program.getFeeType().getExpression())
                .feeAmount(program.getFeeAmount())
                .ageRange(program.getAgeRange())
                .receptionStartDate(program.getReceptionStartDate())
                .receptionEndDate(program.getReceptionEndDate())
                .programStartDate(program.getProgramStartDate())
                .programEndDate(program.getProgramEndDate())
                .institution(program.getInstitution())
                .address(program.getAddress())
                .coordinate(new Coordinate(program.getLocation().getY(), program.getLocation().getX()))
                .gender(program.getGender())
                .receptionMethod(program.getReceptionMethod())
                .receptionUrl(program.getReceptionUrl())
                .build();
    }
}
