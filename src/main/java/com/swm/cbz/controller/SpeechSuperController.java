package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.domain.Evaluation;
import com.swm.cbz.dto.TranscriptDTO;
import com.swm.cbz.service.SpeechSuperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class SpeechSuperController {

    private final TranscriptController transcriptController;
    private final SpeechSuperService speechSuperService;

    public SpeechSuperController(TranscriptController transcriptController, SpeechSuperService speechSuperService) {
        this.transcriptController = transcriptController;
        this.speechSuperService = speechSuperService;
    }

    @PostMapping("/videos/{videoId}/transcripts/{transcriptId}/audio")
    public ApiResponse<Map<String, Object>> getEvaluation(@RequestParam("audio") MultipartFile audioFile, @RequestParam("userId") Long userId, @PathVariable Long videoId, @PathVariable Long transcriptId) {
        TranscriptDTO transcriptDTO = transcriptController.getTranscriptById(videoId, transcriptId).getData();
        String sentence = transcriptDTO.getSentence();
        try {
            byte[] audioData = audioFile.getBytes();
            return speechSuperService.getEvaluation(userId, transcriptId, sentence, audioData);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}/audio/previous")
    public ApiResponse<Map<String,Object>> getPreviousEvaluation(@RequestParam("userId") Long userId, @PathVariable Long videoId, @PathVariable Long transcriptId){
        Optional<Evaluation> evaluationOpt = speechSuperService.findByUserIdAndTranscriptId(userId, transcriptId);

        if (evaluationOpt.isPresent()) {
            Evaluation evaluation = evaluationOpt.get();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("evaluation", evaluation);
            return ApiResponse.success(SuccessMessage.GET_PREVIOUS_EVALUATION, responseData);
        } else {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_EVALUATION_EXCEPTION);
        }
    }

}
