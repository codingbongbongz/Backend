package com.swm.cbz.dto.authorization.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignupResponseDTO {
    String accessToken;
    String refreshToken;
}
