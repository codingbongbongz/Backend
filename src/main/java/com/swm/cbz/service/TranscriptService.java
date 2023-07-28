package com.swm.cbz.service;

import com.swm.cbz.domain.Transcript;
import com.swm.cbz.domain.UserVideo;
import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.TranscriptDTO;
import com.swm.cbz.dto.TranscriptDataDTO;
import com.swm.cbz.dto.TranscriptResponseDTO;
import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import com.swm.cbz.repository.VideoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TranscriptService {
    private final TranscriptRepository transcriptRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final WebClient webClient;

    private final PollyService pollyService;
    private final UserVideoRepository userVideoRepository;
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();

    public TranscriptService(UserRepository userRepository, VideoRepository videoRepository, WebClient.Builder webClientBuilder, TranscriptRepository transcriptRepository, PollyService pollyService, UserVideoRepository userVideoRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
        this.pollyService = pollyService;
        this.userVideoRepository = userVideoRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("http://localhost:5000")  // Set your base URL accordingly
                .build();
    }

    public ResponseEntity<Video> fetchTranscripts(String link, String username) {
        Optional<Users> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Users users = userOptional.get();

        try {
            List<Map> transcriptDataList = webClient.get()
                    .uri("/transcripts/" + link)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();

            Video video = new Video();
            video.setLink(link);
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
                        transcript.setSoundLink(pollyService.synthesizeAndStore((String) transcriptMap.get("text")));
                        transcript.setVideo(video);
                        transcriptList.add(transcript);
                    }
                }
            }
            video.setTranscripts(transcriptList);
            UserVideo userVideo = new UserVideo();
            userVideo.setVideo(video);
            userVideo.setUsers(users);
            userVideoRepository.save(userVideo);
            videoRepository.save(video);
            return new ResponseEntity<>(video, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public TranscriptResponseDTO  getTranscriptsByVideoId(Long videoId) {Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (!optionalVideo.isPresent()) {
           return new TranscriptResponseDTO("자막 조회에 실패하였습니다.", null);
        }

        Video video = optionalVideo.get();
        video.setViews(video.getViews()+1);
        videoRepository.save(video);

        List<Transcript> transcripts = video.getTranscripts();

        List<TranscriptDTO> transcriptDtos = transcripts.stream()
                .map(t -> new TranscriptDTO(t.getTranscriptId(), t.getSentence(), t.getStart(), t.getDuration()))
                .collect(Collectors.toList());

        TranscriptDataDTO data = new TranscriptDataDTO(videoId, transcriptDtos);
        return new TranscriptResponseDTO("자막 조회 성공하였습니다.", data);
    }

    public TranscriptDTO getTranscriptByVideoIdAndTranscriptId(Long videoId, Long transcriptId) {
        Optional<Transcript> optionalTranscript = transcriptRepository.findByTranscriptIdAndVideoVideoId(transcriptId, videoId);

        if (!optionalTranscript.isPresent()) {
            throw new EntityNotFoundException("Transcript not found with id: " + transcriptId + " for video with id: " + videoId);
        }
        Transcript transcript = optionalTranscript.get();
        return new TranscriptDTO(transcript.getTranscriptId(), transcript.getSentence(), transcript.getStart(), transcript.getDuration());
    }
}
