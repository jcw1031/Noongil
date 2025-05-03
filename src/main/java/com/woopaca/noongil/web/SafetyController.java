package com.woopaca.noongil.web;

import com.woopaca.noongil.application.activity.SafetyService;
import com.woopaca.noongil.domain.safety.SafetyStatus;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.RegisterInferenceResultRequest;
import com.woopaca.noongil.web.dto.SafetyStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/safety")
public class SafetyController {

    private final SafetyService safetyService;

    public SafetyController(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @GetMapping
    public ApiResponse<SafetyStatusResponse> getSafetyStatus() {
        SafetyStatus safetyStatus = safetyService.getUserSafetyStatus();
        if (safetyStatus == SafetyStatus.CAUTION) {
            return ApiResults.success(new SafetyStatusResponse("DANGER"));
        }
        return ApiResults.success(new SafetyStatusResponse("SAFE"));
    }

    @PostMapping
    public ApiResponse<Void> registerInferenceResult(@RequestBody RegisterInferenceResultRequest request) {
        safetyService.checkSafety(request.result());
        return ApiResults.success("모델 결과가 등록되었습니다.", null);
    }
}
