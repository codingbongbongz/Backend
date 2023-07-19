package com.swm.cbz.service;

import com.swm.cbz.domain.Transcript;
import com.swm.cbz.domain.User;
import com.swm.cbz.domain.Video;
import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class TranscriptService {
    private final TranscriptRepository transcriptRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final WebClient webClient;

    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();

    public TranscriptService(UserRepository userRepository, VideoRepository videoRepository, WebClient.Builder webClientBuilder, TranscriptRepository transcriptRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("http://localhost:5000")  // Set your base URL accordingly
                .build();
    }

    public Mono<Video> fetchTranscripts(String videoId, String username) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty()) {
            return Mono.error(new RuntimeException("User not found"));
        }
        User user = userOptional.get();
        return webClient.get()
                .uri("/transcripts/" + videoId)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .map(transcriptDataList -> {
                    Video video = new Video();
                    video.setLink("https://www.youtube.com/watch?v=" + videoId);
                    List<Transcript> transcriptList = new ArrayList<>();
                    for (Map<String, Object> transcriptData : transcriptDataList) {
                        List<Map<String, Object>> transcripts = (List<Map<String, Object>>) transcriptData.get("transcripts");
                        if (transcripts != null) {
                            for (Map<String, Object> transcriptMap : transcripts) {
                                Transcript transcript = new Transcript();
                                transcript.setSentence((String) transcriptMap.get("text"));
                                transcript.setStart(((Number) transcriptMap.get("start")).doubleValue());
                                transcript.setDuration(((Number) transcriptMap.get("duration")).doubleValue());
                                String base64Audio = (String) transcriptMap.get("audio");
                                byte[] audioBytes = null;
                                if (base64Audio != null) {
                                    audioBytes = Base64.getDecoder().decode(base64Audio);
                                }
                                /*
                                function for saving audio to S3 and setting soundlink value
                                 */
                                transcript.setVideo(video);
                                transcriptList.add(transcript);
                            }
                        }
                    }
                    video.setTranscripts(transcriptList);
                    return video;
                })
                .flatMap(video -> Mono.fromCallable(() -> videoRepository.save(video)));

    }
}
