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

    private final S3Service s3Service;
    private final UserVideoRepository userVideoRepository;
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();

    public TranscriptService(UserRepository userRepository, VideoRepository videoRepository, WebClient.Builder webClientBuilder, TranscriptRepository transcriptRepository, S3Service s3Service, UserVideoRepository userVideoRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
        this.s3Service = s3Service;
        this.userVideoRepository = userVideoRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }


    public Video fetchTranscripts(String link, Long userId) throws Exception {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        List<Map> youtubeData;
        try {
            youtubeData = webClient.get()
                    .uri("http://localhost:5000/transcripts/" + link)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            throw new Exception("An error occurred while retrieving the transcripts", e);
        }

        Video video = createVideo(link, youtubeData);
        videoRepository.save(video);

        UserVideo userVideo = new UserVideo();
        userVideo.setVideo(video);
        userVideo.setUsers(user);
        userVideoRepository.save(userVideo);

        return video;
    }

    private Video createVideo(String link, List<Map> youtubeData) {
        Video video = new Video();
        video.setLink(link);
        video.setViews(0L);
        video.setIsDefault(true);
        video.setCreatedAt(new Date());
        if (youtubeData != null && !youtubeData.isEmpty()) {
            Map<String, Object> details = youtubeData.get(0);

            video.setVideoTitle((String) details.get("title"));
            video.setCreator((String) details.get("creator"));
            Object durationObject = details.get("duration");
            if (durationObject != null) {
                String durationString = durationObject.toString();
                long durationInSeconds = Long.parseLong(durationString);
                video.setDuration(durationInSeconds);
            }

            Object youtubeViews = details.get("viewCount");
            if (youtubeViews != null) {
                String youtubeViewsString = youtubeViews.toString();
                long youtubeViewsLong = Long.parseLong(youtubeViewsString);
                video.setYoutubeViews(youtubeViewsLong);
            }
        }

        List<Transcript> transcriptList = new ArrayList<>();
        for (Map transcriptData : youtubeData) {
            List<Map<String, Object>> transcripts = (List<Map<String, Object>>) transcriptData.get("transcripts");
            if (transcripts != null) {
                for (Map<String, Object> transcriptMap : transcripts) {
                    Transcript transcript = createTranscript(transcriptMap, video);
                    transcriptList.add(transcript);
                }
            }
        }

        video.setTranscripts(transcriptList);
        return video;
    }


    private Transcript createTranscript(Map<String, Object> transcriptMap, Video video) {
        Transcript transcript = new Transcript();
        transcript.setSentence((String) transcriptMap.get("text"));
        transcript.setStart(((Number) transcriptMap.get("start")).doubleValue());
        transcript.setDuration(((Number) transcriptMap.get("duration")).doubleValue());
        String audioBase64 = (String) transcriptMap.get("audio");
        if (audioBase64 != null) {
            byte[] audioBytes = Base64.getDecoder().decode(audioBase64);
            String fileName = UUID.randomUUID().toString() + ".mp3";
            String s3Url = s3Service.uploadAudio(audioBytes, fileName);
            transcript.setSoundLink(s3Url);
        }
        transcript.setVideo(video);
        return transcript;
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
