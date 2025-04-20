package com.woopaca.noongil.web;

import com.woopaca.noongil.application.user.UserService;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.ChangeConsentsRequest;
import com.woopaca.noongil.web.dto.RegisterContactRequest;
import com.woopaca.noongil.web.dto.RegisterPushTokenRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/contacts")
    public ApiResponse<Void> registerContact(@RequestBody @Validated RegisterContactRequest request) {
        userService.registerUserInfo(request.contact());
        return ApiResults.success("사용자 정보가 등록되었습니다.", null);
    }

    @PostMapping("/push-tokens")
    public ApiResponse<Void> registerPushToken(@RequestBody @Validated RegisterPushTokenRequest request) {
        userService.registerUserPushToken(request.pushToken());
        return ApiResults.success("푸시 토큰이 등록되었습니다.", null);
    }

    @PatchMapping("/consents")
    public ApiResponse<Void> changeUserConsents(@RequestBody @Validated ChangeConsentsRequest request) {
        userService.updateUserConsents(request.push(), request.sms());
        return ApiResults.success("수신 동의가 변경되었습니다.", null);
    }
}
