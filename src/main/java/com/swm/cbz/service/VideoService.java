package com.swm.cbz.service;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.domain.Category;
import com.swm.cbz.domain.CategoryVideo;
import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.video.response.CategoryResponseDTO;
import com.swm.cbz.dto.video.response.CategoryVO;
import com.swm.cbz.dto.video.response.PopularVideoResponseDTO;
import com.swm.cbz.dto.video.response.PopularVideoVO;
import com.swm.cbz.repository.CategoryRepository;
import com.swm.cbz.repository.CategoryVideoRepository;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.swm.cbz.common.response.ErrorMessage.NOT_FOUND_CATEGORY_EXCEPTION;
import static com.swm.cbz.common.response.ErrorMessage.NOT_FOUND_USER_EXCEPTION;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    private final CategoryVideoRepository categoryVideoRepository;

    private final CategoryRepository categoryRepository;

    private final TranscriptService transcriptService;

    public ApiResponse<Video> uploadVideo(Long userId, String link) {
        try {
            Video video = transcriptService.fetchTranscripts(link, userId);
            return ApiResponse.success(SuccessMessage.CREATE_VIDEO_SUCCESS, video);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_USER_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public PopularVideoResponseDTO getPopularVideo(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_EXCEPTION));

        List<Video> popularVideo = videoRepository.findTop5ByOrderByYoutubeViewsDesc();

        List<PopularVideoVO> videoList = popularVideo.stream()
                .map(PopularVideoVO::of)
                .toList();

        return PopularVideoResponseDTO.of(videoList);
    }

    public CategoryResponseDTO getCategoryVideo(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY_EXCEPTION));

        List<CategoryVideo> categoryVideos = categoryVideoRepository.findByCategory_CategoryId(categoryId);
        List<Video> videos = categoryVideos.stream().map(CategoryVideo::getVideo).toList();

        List<CategoryVO> videoList = videos.stream()
                .map(CategoryVO::of)
                .toList();
        return CategoryResponseDTO.of(videoList);

    }
}
