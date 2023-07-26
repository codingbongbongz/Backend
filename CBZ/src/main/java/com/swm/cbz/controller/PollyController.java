package com.swm.cbz.controller;

import com.swm.cbz.domain.Transcript;
import com.swm.cbz.repository.TranscriptRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollyController {

    private final TranscriptRepository transcriptRepository;

    public PollyController(TranscriptRepository transcriptRepository) {
        this.transcriptRepository = transcriptRepository;
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}/audio")
    public ResponseEntity<Resource> getTranscriptAudio(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId) {
        Transcript transcript = transcriptRepository.findByIdAndVideoId(transcriptId, videoId)
                .orElseThrow(() -> new TranscriptNotFoundException("Transcript not found with id " + transcriptId + " for video id " + videoId));

        String audioKey = transcript.getSoundLink();
        String bucketName = "bucket name";
        return pollyService.getAudio(bucketName, audioKey);
    }
}
