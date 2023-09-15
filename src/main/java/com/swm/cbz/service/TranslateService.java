package com.swm.cbz.service;

import com.swm.cbz.domain.Translation;
import com.swm.cbz.dto.TranslationDTO;
import com.swm.cbz.repository.TranslationRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslateService {

    private final TranslateClient translateClient;
    private final TranslationRepository translationRepository;

    public TranslateService(TranslateClient translateClient, TranslationRepository translationRepository) {
        this.translateClient = translateClient;
        this.translationRepository = translationRepository;
    }

    public String translateText(String text, String sourceLang, String targetLang) {
        TranslateTextRequest request = TranslateTextRequest.builder()
                .text(text)
                .sourceLanguageCode(sourceLang)
                .targetLanguageCode(targetLang)
                .build();
        TranslateTextResponse response = translateClient.translateText(request);

        return response.translatedText();
    }

    public List<TranslationDTO> getTranslationsByTranscriptId(Long transcriptId) {
        List<Translation> translationList = translationRepository.findByTranscript_TranscriptId(transcriptId);
        return translationList.stream()
                .map(translation -> new TranslationDTO(
                        translation.getTranslationId(),
                        transcriptId,
                        translation.getText(),
                        translation.getCountry().getCountryName()
                ))
                .collect(Collectors.toList());
    }
}