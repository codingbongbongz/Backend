package com.swm.cbz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptDTO {
    private Long transcriptId;
    private String sentence;
    private Double startTime; // We change it to String to match your JSON response
    private Double duration; // We change it to String to match your JSON response
}