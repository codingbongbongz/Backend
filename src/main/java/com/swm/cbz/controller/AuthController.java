package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.config.resolver.ServiceToken;
import com.swm.cbz.dto.apple.AppleCallbackResponse;
import com.swm.cbz.dto.apple.ApplePayloadVO;
import com.swm.cbz.dto.authorization.request.SignupRequestDTO;
import com.swm.cbz.dto.authorization.response.SignupResponseDTO;
import com.swm.cbz.dto.authorization.response.TokenServiceVO;
import com.swm.cbz.service.AppleService;
import com.swm.cbz.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppleService appleService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDTO> signup(
        @RequestBody SignupRequestDTO requestDTO
    ) {
        SignupResponseDTO data = authService.signupService(requestDTO);
        return ApiResponse.success(SuccessMessage.SIGNUP_SUCCESS, data);
    }

    @PostMapping("/auth/token")
    public ApiResponse<TokenServiceVO> reIssueToken(@ServiceToken TokenServiceVO token) {
        TokenServiceVO data = authService.reIssueToken(token);
        return ApiResponse.success(SuccessMessage.TOKEN_RE_ISSUE_SUCCESS, data);
    }

    @PostMapping("/apple/callback")
    public ResponseEntity<AppleCallbackResponse> appleCallback(@RequestParam("code") String code) {
        AppleCallbackResponse appleInfo = appleService.getAppleInfo(code);
        return ResponseEntity.ok().body(appleInfo);
    }

}
