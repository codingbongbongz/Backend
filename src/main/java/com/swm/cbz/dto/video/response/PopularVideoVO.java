package com.swm.cbz.dto.video.response;

import com.swm.cbz.domain.Video;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PopularVideoVO {
    private Long videoId;
    private String videoUrl;
    private String videoTitle;
    private String creator;
    private Long duration;
    private Long views;
    private Long youtubeViews;

    public static PopularVideoVO of(Video video){
        return PopularVideoVO.builder()
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
