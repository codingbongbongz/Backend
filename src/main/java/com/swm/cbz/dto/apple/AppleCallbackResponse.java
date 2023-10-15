package com.swm.cbz.dto.apple;

import com.swm.cbz.dto.authorization.response.TokenServiceVO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppleCallbackResponse {
    private String accessToken;
    private String refreshToken;

    public static AppleCallbackResponse of(TokenServiceVO token){
        return AppleCallbackResponse.builder()
            .accessToken(token.getAccessToken())
            .refreshToken(token.getRefreshToken())
            .build();
    }
}
