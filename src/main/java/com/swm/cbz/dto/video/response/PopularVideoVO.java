package com.swm.cbz.dto.video.response;

import com.swm.cbz.domain.Video;
import lombok.Builder;

@Builder
public class PopularVideoVO {
    private Long videoId;
    private String videoUrl;

    public static PopularVideoVO of(Video video){
        return PopularVideoVO.builder()
                .videoId(video.getVideoId())
                .videoUrl(video.getLink())
                .build();
    }
}
