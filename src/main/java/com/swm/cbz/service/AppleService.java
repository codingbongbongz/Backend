package com.swm.cbz.service;

import com.swm.cbz.client.AppleWebClient;
import com.swm.cbz.domain.Users;
import com.swm.cbz.dto.apple.AppleCallbackResponse;
import com.swm.cbz.dto.apple.ApplePayloadVO;
import com.swm.cbz.dto.apple.TokenResponse;
import com.swm.cbz.dto.authorization.response.TokenServiceVO;
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
public class AppleService {

    private final AppleWebClient appleWebClient;
    private final UserRepository userRepository;
    private final AuthService authService;

    public AppleCallbackResponse getAppleInfo(String code) {

        String clientSecret = TokenFactory.generateAppleToken();

        ResponseEntity<TokenResponse> response =
            appleWebClient.verifyAppleToken(code, clientSecret);

        String idToken = response.getBody().getIdToken();
        ApplePayloadVO appleData = DecodeTokenFactory.decodePayload(idToken, ApplePayloadVO.class);

        final Users newUser = userRepository.save(Users.builder()
            .email(appleData.getEmail())
            .totalScore(0L)
            .createdAt(LocalDateTime.now())
            .build());

        TokenServiceVO tokenServiceVO = authService.registerToken(newUser);

        return AppleCallbackResponse.of(tokenServiceVO);
    }

}
