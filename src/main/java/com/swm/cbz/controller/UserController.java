package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.controller.exception.NotFoundException;
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
    public ApiResponse<UserVideoResponseDTO> getVideosByUserId(@PathVariable Long userId) {
        try {
            UserVideoResponseDTO data = userService.getVideosByUserId(userId);
            return ApiResponse.success(SuccessMessage.GET_VIDEOS_BY_USER_SUCCESS, data);
        } catch (NotFoundException e) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_USER_EXCEPTION, e.getMessage());
        }
    }


}
