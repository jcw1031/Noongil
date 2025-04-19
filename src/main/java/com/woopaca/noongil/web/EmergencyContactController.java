package com.woopaca.noongil.web;

import com.woopaca.noongil.application.emergency.contact.EmergencyContactService;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.RegisterEmergencyContactRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    public ApiResponse<Void> register(@RequestBody @Validated RegisterEmergencyContactRequest request) {
        emergencyContactService.registerEmergencyContact(request.name(), request.contact());
        return ApiResults.success("비상연락망이 등록되었습니다.", null);
    }
}
