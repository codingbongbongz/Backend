package com.swm.cbz.controller;

import com.swm.cbz.dto.TranscriptDTO;
import com.swm.cbz.dto.TranscriptDataDTO;
import com.swm.cbz.dto.TranscriptResponseDTO;
import com.swm.cbz.service.TranscriptService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranscriptController {
    @Autowired
    private TranscriptService transcriptService;

    @GetMapping("/videos/{videoId}/transcripts")
    public TranscriptResponseDTO getTranscriptsForVideo(@PathVariable("videoId") Long id) {
        return transcriptService.getTranscriptsByVideoId(id);
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}")
    public TranscriptDTO getTranscriptById(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId){
        return transcriptService.getTranscriptByVideoIdAndTranscriptId(videoId,transcriptId);
    }

    /*
    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}")
     tbd
     */
}
