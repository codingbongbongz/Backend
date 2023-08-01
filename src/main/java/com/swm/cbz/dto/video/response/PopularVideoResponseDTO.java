package com.swm.cbz.dto.video.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PopularVideoResponseDTO {
    private List<PopularVideoVO> popularVideo;

    public static PopularVideoResponseDTO of(List<PopularVideoVO> videoList) {
        return PopularVideoResponseDTO.builder()
                .popularVideo(videoList)
                .build();
    }
}
