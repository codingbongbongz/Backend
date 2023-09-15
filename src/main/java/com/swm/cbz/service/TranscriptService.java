package com.swm.cbz.service;

import com.swm.cbz.domain.*;
import com.swm.cbz.dto.TranscriptDTO;
import com.swm.cbz.dto.TranscriptDataDTO;
import com.swm.cbz.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class TranscriptService {
    private static final Logger logger = LoggerFactory.getLogger(TranscriptService.class);

    private final TranscriptRepository transcriptRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final TranslationRepository translationRepository;
    private final TranslateService translateService;

    private final CountryRepository countryRepository;
    private final WebClient webClient;

    private final S3Service s3Service;
    private final UserVideoRepository userVideoRepository;
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();

    List<String> targetLanguages = Arrays.asList("en");
    // List<String> targetLanguages = Arrays.asList("en", "vi", "tl", "zh", "ja");  개느려서 비동기 공부더되기전까진 주석

    public TranscriptService(UserRepository userRepository, VideoRepository videoRepository, WebClient.Builder webClientBuilder, TranscriptRepository transcriptRepository, TranslationRepository translationRepository, TranslateService translateService, CountryRepository countryRepository, S3Service s3Service, UserVideoRepository userVideoRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
        this.translationRepository = translationRepository;
        this.translateService = translateService;
        this.countryRepository = countryRepository;
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
        videoRepository.save(video);
        videoRepository.save(video);
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

        List<CompletableFuture<Transcript>> futures = new ArrayList<>();
        assert youtubeData != null;
        for (Map transcriptData : youtubeData) {
            List<Map<String, Object>> transcripts = (List<Map<String, Object>>) transcriptData.get("transcripts");
            if (transcripts != null) {
                for (Map<String, Object> transcriptMap : transcripts) {
                    futures.add(createTranscript(transcriptMap, video));
                }
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        for (CompletableFuture<Transcript> future : futures) {
            video.getTranscripts().add(future.join());
        }

        return videoRepository.save(video);
    }

    @Async("taskExecutor")
    public CompletableFuture<Transcript> createTranscript(Map<String, Object> transcriptMap, Video video) {
        Transcript transcript = new Transcript();
        transcript.setSentence((String) transcriptMap.get("text"));
        transcript.setStart(((Number) transcriptMap.get("start")).doubleValue());
        transcript.setDuration(((Number) transcriptMap.get("duration")).doubleValue());
        transcript.setVideo(video);
        transcript = transcriptRepository.save(transcript);
        return CompletableFuture.completedFuture(transcript);
    }

    public void audioProcessByTranscript(Transcript transcript) throws Exception {
        List<Map>youtubeData;
        if(transcript.getSoundLink()!=null) return;
        String text = transcript.getSentence();
        try {
            youtubeData = webClient.get()
                    .uri("http://localhost:5000/audio/" + text)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            throw new Exception("An error occurred while retrieving the transcripts", e);
        }
        Map<String, Object> details = youtubeData.get(0);
        String audioBase64 = (String) details.get("audio");
        if (audioBase64 != null) {
            byte[] audioBytes = Base64.getDecoder().decode(audioBase64);
            String fileName = UUID.randomUUID().toString() + ".mp3";
            String s3Url = s3Service.uploadAudio(audioBytes, fileName);
            transcript.setSoundLink(s3Url);
        }
    }
    public void translationProcessByTranscript(Transcript transcript) {
        List<Translation> translations = translationRepository.findByTranscript_TranscriptId(transcript.getTranscriptId());

        if (!translations.isEmpty()) {
            System.out.println(translations);
            return;
        }

        String text = transcript.getSentence();
        for (String country : targetLanguages) {
            String translationText = translateService.translateText(text, "ko", country);
            Translation translation = new Translation();
            translation.setTranscript(transcript);
            translation.setCountry(countryRepository.findByCountryCode(country));
            translation.setText(translationText);

            System.out.println(translation);

            translationRepository.save(translation);
        }
    }

    public TranscriptDataDTO getTranscriptsByVideoId(Long videoId) {
        return videoRepository.findById(videoId)
                .map(video -> {
                    video.setViews(video.getViews() + 1);
                    videoRepository.save(video);
                    List<TranscriptDTO> transcriptDtos = video.getTranscripts().stream()
                            .sorted(Comparator.comparing(Transcript::getStart)) // Sort by start
                            .map(t -> new TranscriptDTO(t.getTranscriptId(), t.getSentence(), t.getStart(), t.getDuration()))
                            .collect(Collectors.toList());
                    return new TranscriptDataDTO(videoId, transcriptDtos);
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
