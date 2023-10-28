package com.swm.cbz.service;

import com.google.gson.Gson;
import com.swm.cbz.client.AppleWebClient;
import com.swm.cbz.client.GoogleWebClient;
import com.swm.cbz.domain.Users;
import com.swm.cbz.dto.apple.AppleCallbackResponse;
import com.swm.cbz.dto.apple.ApplePayloadVO;
import com.swm.cbz.dto.apple.TokenResponse;
import com.swm.cbz.dto.authorization.response.TokenServiceVO;
import com.swm.cbz.dto.google.request.GoogleLoginRequest;
import com.swm.cbz.dto.google.response.GoogleLoginResponse;
import com.swm.cbz.dto.google.response.GoogleTokenClientResponse;
import com.swm.cbz.dto.google.response.GoogleUserInfoResponse;
import com.swm.cbz.factory.DecodeTokenFactory;
import com.swm.cbz.factory.TokenFactory;
import com.swm.cbz.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final GoogleWebClient googleWebClient;

    public GoogleLoginResponse googleLogin(GoogleLoginRequest request) {

        ResponseEntity<GoogleTokenClientResponse> googleToken =
            googleWebClient.getGoogleToken(request.getCode());

        ResponseEntity<GoogleUserInfoResponse> googleUserInfo =
            googleWebClient.getGoogleUserInfo(googleToken.getBody().getAccessToken());

        Gson gson = new Gson();
        GoogleUserInfoResponse response =
            gson.fromJson(googleToken.toString(), GoogleUserInfoResponse.class);

        return GoogleLoginResponse.of(response);

    }

}
