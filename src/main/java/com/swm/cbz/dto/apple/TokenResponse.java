package com.swm.cbz.dto.apple;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse {
    private String accessToken;
    private Integer expiresIn;
    private String idToken;
    private String refreshToken;
    private String tokenType;
}
