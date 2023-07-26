package com.swm.cbz.controller;

import com.swm.cbz.dto.UserVideoResponseDTO;
import com.swm.cbz.service.UserService;
import com.swm.cbz.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final VideoService videoService;
    private final UserService userService;

    public UserController(VideoService videoService, UserService userService) {
        this.videoService = videoService;
        this.userService = userService;
    }

    @GetMapping("/videos/{userId}")
    public ResponseEntity<UserVideoResponseDTO> getVideosByUserId(@PathVariable Long userId) {
        UserVideoResponseDTO response = userService.getVideosByUserId(userId);
        return ResponseEntity.ok(response);
    }
    
}
