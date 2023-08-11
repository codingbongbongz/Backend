package com.swm.cbz.dto.video.response;

import com.swm.cbz.domain.Video;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryVO {
    private Long videoId;
    private String videoUrl;

    public static CategoryVO of(Video video){
        return CategoryVO.builder()
                .videoId(video.getVideoId())
                .videoUrl(video.getLink())
                .build();
    }
}
