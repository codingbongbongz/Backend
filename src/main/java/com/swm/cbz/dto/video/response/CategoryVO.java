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
    private String videoTitle;
    private String creator;
    private Long duration;
    private Long views;
    private Long youtubeViews;
    public static CategoryVO of(Video video){
        return CategoryVO.builder()
                .videoId(video.getVideoId())
                .videoUrl(video.getLink())
                .videoTitle(video.getVideoTitle())
                .creator(video.getCreator())
                .duration(video.getDuration())
                .views(video.getViews())
                .youtubeViews(video.getYoutubeViews())
                .build();
    }
}
