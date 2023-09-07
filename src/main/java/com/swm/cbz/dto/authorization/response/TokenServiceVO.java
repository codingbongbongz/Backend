package com.swm.cbz.dto.authorization.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenServiceVO {
    String accessToken;
    String refreshToken;

}
