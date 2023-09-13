package com.swm.cbz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String fetchOpenAI(String text) throws Exception{
        String s;
        try {
            String flaskUrl = "http://localhost:5000/generate_text";
            Map<String, String> body = new HashMap<>();
            body.put("input_text", text);

            s = webClient.post()
                    .uri(flaskUrl)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            Map responseMap = objectMapper.readValue(s, Map.class);
            return (String) responseMap.get("generated_text");
        } catch (Exception e) {
            logger.error("An error occurred while fetching generated text", e);
            throw new Exception("An error occurred while fetching generated text", e);
        }
    }

    public List<String> fetchExamples(String text) throws Exception {
        String s;
        List<String> sentenceList = new ArrayList<>();
        try {
            String generatedText = fetchOpenAI(text);
            Pattern pattern = Pattern.compile("\\d+\\.\\s");

            Matcher matcher = pattern.matcher(generatedText);
            String cleanedText = matcher.replaceAll("");
            String[] sentences = cleanedText.split("\\.\\s*|\n");
            for (String sentence : sentences) {
                if (!sentence.isEmpty()) {
                    sentenceList.add(sentence.trim());
                }
            }
            logger.info("Successfully fetched and parsed generated text: {}", sentenceList);

        } catch (Exception e) {
            logger.error("An error occurred while fetching generated text", e);
            throw new Exception("An error occurred while fetching generated text", e);
        }

        return sentenceList;
    }

    public List<String> fetchNouns(String sentence) throws Exception {
        String s;
        List<String> nounList = new ArrayList<>();
        try {
            String generatedText = fetchOpenAI(sentence);
            return Arrays.asList(generatedText.split("\\s+"));

        } catch (Exception e) {
            logger.error("An error occurred while fetching generated text", e);
            throw new Exception("An error occurred while fetching generated text", e);
        }
    }
}
