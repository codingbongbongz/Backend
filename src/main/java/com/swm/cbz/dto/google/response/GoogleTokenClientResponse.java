package com.swm.cbz.dto.google.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenClientResponse {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
    private String tokenType;
    private String scope;
}
