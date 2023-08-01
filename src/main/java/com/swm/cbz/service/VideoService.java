package com.swm.cbz.service;

import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.video.response.PopularVideoResponseDTO;
import com.swm.cbz.dto.video.response.PopularVideoVO;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.swm.cbz.common.response.ErrorMessage.NOT_FOUND_USER_EXCEPTION;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TranscriptService transcriptService;

    public ResponseEntity<Video> uploadVideo(String link, Long userId){
        Optional<Users> userOptional = userService.searchUserById(userId);
        if(!userOptional.isPresent()){
            throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
        }
        return transcriptService.fetchTranscripts(link, userOptional.get().getUserId());
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

}
