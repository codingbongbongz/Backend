package com.swm.cbz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoDetailsDTO {
    private String title;
    private String creator;
    private Double duration;
    private Long youtubeViews;
    private List<TranscriptDTO> transcripts;

}
