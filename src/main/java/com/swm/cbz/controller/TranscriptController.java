package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.dto.TranscriptDTO;
import com.swm.cbz.dto.TranscriptDataDTO;
import com.swm.cbz.dto.TranscriptResponseDTO;
import com.swm.cbz.service.TranscriptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
public class TranscriptController {

    private final TranscriptService transcriptService;

    public TranscriptController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping("/videos/{videoId}/transcripts")
    public ApiResponse<TranscriptDataDTO> getTranscriptsForVideo(@PathVariable("videoId") Long id) {
        try {
            TranscriptDataDTO data = transcriptService.getTranscriptsByVideoId(id);
            return ApiResponse.success(SuccessMessage.GET_TRANSCRIPT_SUCCESS, data);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.TRANSCRIPT_NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}")
    public ApiResponse<TranscriptDTO> getTranscriptById(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId) {
        try {
            TranscriptDTO data = transcriptService.getTranscriptByVideoIdAndTranscriptId(videoId, transcriptId);
            return ApiResponse.success(SuccessMessage.GET_TRANSCRIPT_SUCCESS, data);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.TRANSCRIPT_NOT_FOUND, e.getMessage());
        }
    }

    /*
    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}")
     tbd
     */
}
