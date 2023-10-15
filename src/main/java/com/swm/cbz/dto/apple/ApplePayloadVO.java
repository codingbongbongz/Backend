package com.swm.cbz.dto.apple;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplePayloadVO {
    private String sub;
    private String email;
}
