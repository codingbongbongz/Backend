package com.swm.cbz.controller;

import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.LinkUploadDTO;
import com.swm.cbz.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoController {

    private VideoService videoService;
    @PostMapping("/upload")
    public ResponseEntity<Video> uploadVideo(@RequestBody LinkUploadDTO linkUploadDTO){
        String link = linkUploadDTO.getLink();
        String username = linkUploadDTO.getUsername();
        return videoService.uploadVideo(link, username);
    }
}
