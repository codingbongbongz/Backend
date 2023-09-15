package com.swm.cbz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swm.cbz.domain.Examples;
import com.swm.cbz.domain.Word;
import com.swm.cbz.repository.ExampleRepository;
import com.swm.cbz.repository.WordRepository;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptService.class);

    private final WebClient webClient;

    private final WordRepository wordRepository;
    private final ExampleRepository exampleRepository;

    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();
    public OpenAIService(WebClient.Builder webClientBuilder, WordRepository wordRepository, ExampleRepository exampleRepository) {
        this.wordRepository = wordRepository;
        this.exampleRepository = exampleRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
    @Transactional
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
    @Transactional
    public List<String> fetchExamples(String text) throws Exception {
        String inputText = "Give me 5 examples of where this korean word is used in a korean sentence without english translations, just labeled 1. 2. 3. 4. 5. : '" + text + "'";
        List<String> sentenceList = new ArrayList<>();
        Word word = wordRepository.findByWord(text);
        try {
            String generatedText = fetchOpenAI(inputText);
            Pattern pattern = Pattern.compile("\\d+\\.\\s");

            Matcher matcher = pattern.matcher(generatedText);
            String cleanedText = matcher.replaceAll("");
            String[] sentences = cleanedText.split("\\.\\s*|\n");
            for (String sentence : sentences) {
                if (!sentence.isEmpty()) {
                    Examples example = new Examples();
                    example.setWord(word);
                    example.setSentence(sentence.trim());
                    exampleRepository.save(example);
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
    @Transactional
    public List<String> fetchNouns(String sentence) throws Exception {
        String s;
        List<String> nounList;
        try {
            String generatedText = fetchOpenAI(sentence);
            nounList = Arrays.asList(generatedText.split("\\s+"));
            for(String noun : nounList){
                if(!wordRepository.existsByWord(noun)) {
                    Word word = new Word();
                    word.setWord(noun);
                    wordRepository.save(word);
                    fetchExamples(noun);
                }
            }
            return nounList;
        } catch (Exception e) {
            logger.error("An error occurred while fetching generated text", e);
            throw new Exception("An error occurred while fetching generated text", e);
        }

    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<String> getExamples(String word) {
        Word wordEntity = wordRepository.findByWord(word);
        entityManager.refresh(wordEntity);
        Hibernate.initialize(wordEntity.getExamples());
        List<String> exampleList = new ArrayList<>();
        Set<Examples> examples = wordEntity.getExamples();
        for (Examples example : examples) {
            exampleList.add(example.getSentence());
        }
        return exampleList;
    }
}
