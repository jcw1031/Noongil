package com.woopaca.noongil.web;

import com.woopaca.noongil.application.emergency.contact.EmergencyContactService;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.ChangeNotificationRequest;
import com.woopaca.noongil.web.dto.EmergencyContactListResponse;
import com.woopaca.noongil.web.dto.RegisterEmergencyContactRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

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

    @GetMapping
    public ApiResponse<Collection<EmergencyContactListResponse>> getRegisteredEmergencyContacts() {
        List<EmergencyContactListResponse> response = emergencyContactService.findRegisteredEmergencyContacts()
                .stream()
                .map(EmergencyContactListResponse::from)
                .toList();
        return ApiResults.success(response);
    }

    @PutMapping("/{contactId}/notifications")
    public ApiResponse<Void> changeNotification(@RequestBody ChangeNotificationRequest request,
                                                @PathVariable("contactId") Long contactId) {
        emergencyContactService.changeNotification(contactId, request.notification());
        return ApiResults.success("알림 설정이 변경되었습니다.", null);
    }

    @DeleteMapping("/{contactId}")
    public ApiResponse<Void> delete(@PathVariable("contactId") Long contactId) {
        emergencyContactService.deleteEmergencyContact(contactId);
        return ApiResults.success("비상연락망이 삭제되었습니다.", null);
    }
}
