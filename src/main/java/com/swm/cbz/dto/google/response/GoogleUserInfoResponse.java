package com.swm.cbz.dto.google.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleUserInfoResponse {

    private String email;
    private String name;
    private String familyName;
    private String givenName;
    private String gender;
    private String sub;
    private String picture;
}
