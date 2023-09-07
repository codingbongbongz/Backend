package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.config.resolver.UserId;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.UserDTO;
import com.swm.cbz.dto.UserVideoResponseDTO;
import com.swm.cbz.service.UserService;
import com.swm.cbz.service.VideoService;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final VideoService videoService;
    private final UserService userService;

    public UserController(VideoService videoService, UserService userService) {
        this.videoService = videoService;
        this.userService = userService;
    }

    @GetMapping("/videos")
    public ApiResponse<List<Video>> getVideosByUserId(@UserId Long userId) {
        try {
            List<Video> data = userService.getVideosByUserId(userId);
            return ApiResponse.success(SuccessMessage.GET_VIDEOS_BY_USER_SUCCESS, data);
        } catch (NotFoundException e) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_USER_EXCEPTION, e.getMessage());
        }
    }

    @PatchMapping("/mypage")
    public ApiResponse<Users> patchUserProfile(@UserId Long userId, @ModelAttribute UserDTO userDTO){
        return userService.updateUserProfile(userId, userDTO);
    }

    @DeleteMapping("/mypage")
    public ApiResponse deleteUser(@UserId Long userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping("/user")
    public ApiResponse<Users> getUser(@UserId Long userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/user/all")
    public ApiResponse<List<Users>> getUsers(){
        return userService.getAllUsers();
    }

}
