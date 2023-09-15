package com.swm.cbz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TranslationDTO {
    private Long translationId;
    private Long transcriptId;
    private String text;
    private String country;
}
