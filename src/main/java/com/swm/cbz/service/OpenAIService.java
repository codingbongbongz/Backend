package com.swm.cbz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptService.class);

    private final WebClient webClient;

    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();
    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    public String fetchOpenAIResponse(String text) throws Exception {
        String s;
        try{
            String flaskUrl = "http://localhost:6000/generate_text";
            Map<String, String> body = new HashMap<>();
            body.put("input_text", text);

            s = webClient.post()
                    .uri(flaskUrl)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            logger.info("Successfully fetched generated text: {}", s);
        } catch (Exception e) {
            logger.error("An error occurred while fetching generated text", e);
            throw new Exception("An error occurred while fetching generated text", e);
        }
        return s;
    }
}
