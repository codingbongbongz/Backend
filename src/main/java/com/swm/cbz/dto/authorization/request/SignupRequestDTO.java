package com.swm.cbz.dto.authorization.request;

import lombok.Getter;

@Getter
public class SignupRequestDTO {

    String email;
    String name;
    String nickname;
    String password;
    String introduce;
    String country;
}
