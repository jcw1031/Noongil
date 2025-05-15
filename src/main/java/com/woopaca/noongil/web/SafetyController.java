package com.woopaca.noongil.web;

import com.woopaca.noongil.application.activity.SafetyService;
import com.woopaca.noongil.domain.safety.Safety;
import com.woopaca.noongil.domain.safety.SafetyStatus;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.ContactSafetyRequest;
import com.woopaca.noongil.web.dto.LastSafetyDateResponse;
import com.woopaca.noongil.web.dto.RegisterInferenceResultRequest;
import com.woopaca.noongil.web.dto.ResponseSafetyRequest;
import com.woopaca.noongil.web.dto.SafetyStatusResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        return ApiResults.success(new SafetyStatusResponse(safetyStatus.name()));
    }

    @PostMapping
    public ApiResponse<Void> registerInferenceResult(@RequestBody RegisterInferenceResultRequest request) {
        safetyService.checkSafety(request.result());
        return ApiResults.success("모델 결과가 등록되었습니다.", null);
    }

    @PostMapping("/response") // 사용자의 "괜찮아요" 응답
    public ApiResponse<Void> responseSafety(@RequestBody ResponseSafetyRequest request) {
        safetyService.responseSafety(request.response());
        return ApiResults.success("모델 결과가 등록되었습니다.", null);
    }

    @PostMapping("/contact")
    public ApiResponse<Void> contactSafety(@RequestBody @Validated ContactSafetyRequest request) {
        safetyService.contactSafety(request.contact());
        return ApiResults.success("비상연락망에 연락을 시도했습니다.", null);
    }

    @GetMapping("/last-date")
    public ApiResponse<LastSafetyDateResponse> getLastSafetyDate() {
        LocalDate lastSafetyDate = safetyService.findUserLastSafety()
                .map(Safety::getCreatedAt)
                .map(LocalDateTime::toLocalDate)
                .orElse(null);
        return ApiResults.success(new LastSafetyDateResponse(lastSafetyDate));
    }
}
