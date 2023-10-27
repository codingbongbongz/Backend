package com.swm.cbz.dto.google.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleLoginResponse {
    private String email;
    private String name;
    private String profileImageUrl;
    private String social;

    public static GoogleLoginResponse of(GoogleUserInfoResponse response) {
        return GoogleLoginResponse.builder()
            .email(response.getEmail())
            .name(response.getName())
            .profileImageUrl(response.getPicture())
            .social("Google")
            .build();
    }
}
