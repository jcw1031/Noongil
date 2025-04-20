package com.woopaca.noongil.web;

import com.woopaca.noongil.application.user.UserService;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.RegisterContactRequest;
import org.springframework.validation.annotation.Validated;
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
    public ApiResults.ApiResponse<Void> registerContact(@RequestBody @Validated RegisterContactRequest request) {
        userService.updateUserInfo(request.contact());
        return ApiResults.success("사용자 정보가 등록되었습니다.", null);
    }
}
