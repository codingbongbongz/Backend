package com.swm.cbz.service;

import com.swm.cbz.domain.Transcript;
import com.swm.cbz.domain.User;
import com.swm.cbz.domain.UserVideo;
import com.swm.cbz.domain.Video;
import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import com.swm.cbz.repository.VideoRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final UserVideoRepository userVideoRepository;
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();

    public TranscriptService(UserRepository userRepository, VideoRepository videoRepository, WebClient.Builder webClientBuilder, TranscriptRepository transcriptRepository, UserVideoRepository userVideoRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
        this.userVideoRepository = userVideoRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("http://localhost:5000")  // Set your base URL accordingly
                .build();
    }

    public ResponseEntity<Video> fetchTranscripts(String link, String username) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        try {
            List<Map> transcriptDataList = webClient.get()
                    .uri("/transcripts/" + link)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();

            Video video = new Video();
            video.setLink("https://www.youtube.com/watch?v=" + link);
            List<Transcript> transcriptList = new ArrayList<>();
            for (Map transcriptData : transcriptDataList) {
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
            UserVideo userVideo = new UserVideo();
            userVideo.setVideo(video);
            userVideo.setUser(user);
            userVideoRepository.save(userVideo);
            videoRepository.save(video);
            return new ResponseEntity<>(video, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
