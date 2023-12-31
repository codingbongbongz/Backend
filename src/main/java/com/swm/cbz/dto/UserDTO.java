package com.swm.cbz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    private String name;
    private String password;
    private String email;
    private String nickname;
    private Long countryId;
    private String social;
    private String introduce;
    private Long totalScore;
    private MultipartFile profileImage;
}
