package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.dto.authorization.request.SignupRequestDTO;
import com.swm.cbz.dto.authorization.response.SignupResponseDTO;
import com.swm.cbz.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDTO> signup(
        @RequestBody SignupRequestDTO requestDTO
    ) {
        SignupResponseDTO data = authService.signupService(requestDTO);
        return ApiResponse.success(SuccessMessage.SIGNUP_SUCCESS, data);
    }

}
