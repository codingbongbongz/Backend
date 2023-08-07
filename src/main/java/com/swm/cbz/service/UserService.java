package com.swm.cbz.service;

import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.domain.UserVideo;
import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.UserVideoResponseDTO;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.swm.cbz.common.response.ErrorMessage.NOT_FOUND_USER_EXCEPTION;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserVideoRepository userVideoRepository;
    public UserService(UserRepository userRepository, UserVideoRepository userVideoRepository) {
        this.userRepository = userRepository;
        this.userVideoRepository = userVideoRepository;
    }

    public Optional<Users> searchUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<Video> getVideosByUserId(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_EXCEPTION));

        List<UserVideo> userVideos = userVideoRepository.findByUsers(user);
        List<Video> videos = userVideos.stream()
                .map(UserVideo::getVideo)
                .collect(Collectors.toList());
        UserVideoResponseDTO responseDTO = new UserVideoResponseDTO();
        return videos;
    }

}
