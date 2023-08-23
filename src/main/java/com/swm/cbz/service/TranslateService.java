package com.swm.cbz.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateAsyncClient;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

import java.util.concurrent.CompletableFuture;

@Service
public class TranslateService {

    private final TranslateAsyncClient translateAsyncClient;

    public TranslateService(TranslateAsyncClient translateAsyncClient) {
        this.translateAsyncClient = translateAsyncClient;
    }

    public CompletableFuture<String> translateText(String text, String sourceLang, String targetLang) {
        TranslateTextRequest request = TranslateTextRequest.builder()
                .text(text)
                .sourceLanguageCode(sourceLang)
                .targetLanguageCode(targetLang)
                .build();

        return translateAsyncClient.translateText(request)
                .thenApply(TranslateTextResponse::translatedText);
    }
}