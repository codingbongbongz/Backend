package com.swm.cbz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptResponseDTO {

    private String message;
    private TranscriptDataDTO data;
}
