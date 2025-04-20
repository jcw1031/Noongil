package com.woopaca.noongil.web;

import com.woopaca.noongil.application.auth.AuthenticationService;
import com.woopaca.noongil.application.auth.SignInResult;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.SignInRequest;
import com.woopaca.noongil.web.dto.SignInResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/apple")
    public ApiResponse<SignInResponse> appleSignIn(@RequestBody @Validated SignInRequest request) {
        SignInResult signInResult = authenticationService
                .authenticateUser(request.authorizationCode(), request.name(), request.email());
        return ApiResults.success(SignInResponse.from(signInResult));
    }
}
