package com.woopaca.noongil.web;

import com.woopaca.noongil.domain.program.Program;
import com.woopaca.noongil.domain.program.ProgramRepository;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.ProgramDetailResponse;
import com.woopaca.noongil.web.dto.ProgramListResponse;
import com.woopaca.noongil.web.dto.ProgramsCountByBoroughResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/programs")
public class ProgramController {

    private final ProgramRepository programRepository;

    public ProgramController(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @GetMapping("/summary")
    public ApiResponse<Collection<ProgramsCountByBoroughResponse>> getCountByBorough() {
        List<ProgramsCountByBoroughResponse> response = programRepository.countEachBorough()
                .stream()
                .map(ProgramsCountByBoroughResponse::from)
                .toList();
        return ApiResults.success(response);
    }

    @GetMapping
    public ApiResponse<Collection<ProgramListResponse>> getAllPrograms(
            @RequestParam(value = "borough", required = false) String borough
    ) {
        if (!StringUtils.hasText(borough)) {
            List<ProgramListResponse> response = programRepository.findAll()
                    .stream()
                    .map(ProgramListResponse::from)
                    .toList();
            return ApiResults.success(response);
        }

        List<ProgramListResponse> response = programRepository.findByBorough(borough)
                .stream()
                .map(ProgramListResponse::from)
                .toList();
        return ApiResults.success(response);
    }

    @GetMapping("/{programId}")
    public ApiResponse<ProgramDetailResponse> getProgram(@PathVariable("programId") Long programId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new IllegalArgumentException("프로그램을 찾을 수 없습니다. programId: " + programId));
        ProgramDetailResponse response = ProgramDetailResponse.from(program);
        return ApiResults.success(response);
    }
}
