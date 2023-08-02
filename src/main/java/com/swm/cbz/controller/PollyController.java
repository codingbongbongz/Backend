package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.domain.Transcript;
import com.swm.cbz.repository.TranscriptRepository;
import com.swm.cbz.service.PollyService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
public class PollyController {

    private final TranscriptRepository transcriptRepository;
    private final PollyService pollyService;

    public PollyController(TranscriptRepository transcriptRepository, PollyService pollyService) {
        this.transcriptRepository = transcriptRepository;
        this.pollyService = pollyService;
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}/audio")
    public ApiResponse<Resource> getTranscriptAudio(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId) {
        return pollyService.getTranscriptAudio(videoId, transcriptId);
    }

}
