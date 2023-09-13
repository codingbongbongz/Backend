package com.swm.cbz.controller;

import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.service.S3Service;
import com.swm.cbz.service.TranscriptService;
import com.swm.cbz.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollyController {

    private final TranscriptRepository transcriptRepository;
    private final S3Service s3Service;

    private final TranscriptService transcriptService;
    public PollyController(TranscriptRepository transcriptRepository, S3Service s3Service, TranscriptService transcriptService) {
        this.transcriptRepository = transcriptRepository;
        this.s3Service = s3Service;
        this.transcriptService = transcriptService;
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}/audio")
    public ResponseEntity<byte[]> getTranscriptAudio(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId) throws Exception {
        transcriptService.audioProcessByTranscript(transcriptRepository.findByTranscriptId(transcriptId));
        return s3Service.getTranscriptAudio(videoId, transcriptId);
    }
}
