package com.swm.cbz.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateAsyncClient;
import software.amazon.awssdk.services.translate.TranslateClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsTranslateConfig {

    @Bean
    public TranslateAsyncClient translateAsyncClient() {
        return TranslateAsyncClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
