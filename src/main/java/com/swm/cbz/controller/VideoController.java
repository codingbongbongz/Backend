package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.config.resolver.UserId;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.LinkUploadDTO;
import com.swm.cbz.dto.UserVideoDTO;
import com.swm.cbz.dto.video.response.CategoryResponseDTO;
import com.swm.cbz.dto.video.response.PopularVideoResponseDTO;
import com.swm.cbz.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/upload")
    public ApiResponse<Video> uploadVideo(@UserId Long userId, @RequestParam String link){
        return videoService.uploadVideo(userId, link);
    }

    @GetMapping("/videos/popular")
    public ApiResponse<PopularVideoResponseDTO> getPopularVideo(
            @UserId Long userId
    ) {
        PopularVideoResponseDTO data = videoService.getPopularVideo(userId);
        return ApiResponse.success(SuccessMessage.GET_POPULAR_VIDEO_SUCCESS, data);
    }

    @GetMapping("/videos/categories")
    public ApiResponse<List<CategoryResponseDTO>> getCategoriesVideo(@RequestParam List<Long> categoryIds) {
        List<CategoryResponseDTO> data = videoService.getCategoriesVideo(categoryIds);
        return ApiResponse.success(SuccessMessage.GET_CATEGORY_VIDEO_SUCCESS, data);
    }


    @PostMapping("/videos")
    public ApiResponse<UserVideoDTO> viewVideo(@UserId Long userId, @RequestParam Long videoId) throws Exception {
        UserVideoDTO data = videoService.viewVideo(userId, videoId);
        return ApiResponse.success(SuccessMessage.VIEW_VIDEO_SUCCESS, data);
    }
}
