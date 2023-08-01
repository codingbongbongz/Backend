package com.swm.cbz.controller;

import com.swm.cbz.domain.Evaluation;
import com.swm.cbz.dto.TranscriptDTO;
import com.swm.cbz.service.SpeechSuperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class SpeechSuperController {

    private final TranscriptController transcriptController;
    private final SpeechSuperService speechSuperService;

    public SpeechSuperController(TranscriptController transcriptController, SpeechSuperService speechSuperService) {
        this.transcriptController = transcriptController;
        this.speechSuperService = speechSuperService;
    }

    @PostMapping("/videos/{videoId}/transcripts/{transcriptId}/audio")
    public ResponseEntity<Evaluation> getEvaluation(@RequestParam("audio") MultipartFile audioFile, @RequestParam("userId") Long userId, @PathVariable Long videoId, @PathVariable Long transcriptId){
        TranscriptDTO transcriptDTO = transcriptController.getTranscriptById(videoId, transcriptId);
        String sentence = transcriptDTO.getSentence();
        try {
            byte[] audioData = audioFile.getBytes();
            return speechSuperService.getEvaluation(userId, transcriptId, sentence, audioData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
}
