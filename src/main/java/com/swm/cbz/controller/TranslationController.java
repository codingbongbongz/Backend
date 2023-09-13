package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.domain.Transcript;
import com.swm.cbz.domain.Translation;
import com.swm.cbz.dto.TranslationDTO;
import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.service.TranscriptService;
import com.swm.cbz.service.TranslateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class TranslationController {
    private final TranscriptService transcriptService;
    private final TranslateService translateService;
    private final TranscriptRepository transcriptRepository;

    public TranslationController(TranscriptService transcriptService, TranslateService translateService, TranscriptRepository transcriptRepository) {
        this.transcriptService = transcriptService;
        this.translateService = translateService;
        this.transcriptRepository = transcriptRepository;
    }

    @Transactional
    @GetMapping("/translations/{transcriptId}")
    public ApiResponse<List<TranslationDTO>> getTranslations(@PathVariable Long transcriptId) {
        Transcript transcript = transcriptRepository.findByTranscriptId(transcriptId);
        transcriptService.translationProcessByTranscript(transcript);
        List<TranslationDTO> translations = translateService.getTranslationsByTranscriptId(transcriptId);
        if (translations.isEmpty()) {
            return ApiResponse.error(ErrorMessage.TRANSLATIONS_NOT_FOUND, "No translations found for given transcriptId");
        } else {
            return ApiResponse.success(SuccessMessage.GET_TRANSLATIONS_SUCCESS, translations);
        }
    }

}
