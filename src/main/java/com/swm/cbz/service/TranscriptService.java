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


    public Video fetchTranscripts(String link, Long userId) throws Exception {
        Optional<Users> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
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
                        //String base64Audio = (String) transcriptMap.get("audio");
                        //byte[] audioBytes = null;
                        //if (base64Audio != null) {
                        //    audioBytes = Base64.getDecoder().decode(base64Audio);
                        //}
                        //transcript.setSoundLink(pollyService.synthesizeAndStore((String) transcriptMap.get("text")));
                        transcript.setVideo(video);
                        transcriptList.add(transcript);
                    }
                }
            }
            video.setTranscripts(transcriptList);
            videoRepository.save(video);
            UserVideo userVideo = new UserVideo();
            userVideo.setVideo(video);
            userVideo.setUsers(users);
            userVideoRepository.save(userVideo);
            return video;
        } catch (Exception e) {
            throw new Exception("An error occurred while processing the transcripts", e);
        }
    }

    private static final String TRANSCRIPTS_NOT_FOUND = "자막 조회에 실패하였습니다.";
    private static final String TRANSCRIPTS_FOUND = "자막 조회 성공하였습니다.";

    public TranscriptResponseDTO getTranscriptsByVideoId(Long videoId) {
        return videoRepository.findById(videoId)
                .map(video -> {
                    video.setViews(video.getViews() + 1);
                    videoRepository.save(video);
                    List<TranscriptDTO> transcriptDtos = video.getTranscripts().stream()
                            .map(t -> new TranscriptDTO(t.getTranscriptId(), t.getSentence(), t.getStart(), t.getDuration()))
                            .collect(Collectors.toList());
                    TranscriptDataDTO data = new TranscriptDataDTO(videoId, transcriptDtos);
                    return new TranscriptResponseDTO(TRANSCRIPTS_FOUND, data);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transcripts not found for video with id: " + videoId));
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
