package com.swm.cbz.dto.video.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CategoryResponseDTO {
    private List<CategoryVO> categoryVideo;

    public static CategoryResponseDTO of(List<CategoryVO> videoList) {
        return CategoryResponseDTO.builder()
                .categoryVideo(videoList)
                .build();
    }
}
