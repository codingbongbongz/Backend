package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.service.S3Service;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollyController {

    private final TranscriptRepository transcriptRepository;
    private final S3Service s3Service;

    public PollyController(TranscriptRepository transcriptRepository, S3Service s3Service) {
        this.transcriptRepository = transcriptRepository;
        this.s3Service = s3Service;
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}/audio")
    public ResponseEntity<byte[]> getTranscriptAudio(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId) {
        return s3Service.getTranscriptAudio(videoId, transcriptId);
    }

}
