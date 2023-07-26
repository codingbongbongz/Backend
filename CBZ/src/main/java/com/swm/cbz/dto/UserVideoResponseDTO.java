package com.swm.cbz.dto;

import com.swm.cbz.domain.Video;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserVideoResponseDTO {

    private String message;
    private List<Video> data;

}