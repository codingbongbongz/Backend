package com.swm.cbz.dto;

import com.swm.cbz.domain.Transcript;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptDataDTO {

    private Long videoId;
    private List<TranscriptDTO> transcripts;
}