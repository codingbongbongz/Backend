package com.swm.cbz.service;

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

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserVideoRepository userVideoRepository;
    public UserService(UserRepository userRepository, UserVideoRepository userVideoRepository) {
        this.userRepository = userRepository;
        this.userVideoRepository = userVideoRepository;
    }

    public Optional<Users> searchUserById(String userId) {
        return userRepository.findById(userId);
    }

    public UserVideoResponseDTO getVideosByUserId(String userId) {
        List<UserVideo> userVideos = userVideoRepository.findByUsersUsersId(userId);
        List<Video> videos = userVideos.stream()
                .map(UserVideo::getVideo)
                .collect(Collectors.toList());
        UserVideoResponseDTO responseDTO = new UserVideoResponseDTO();
        responseDTO.setMessage("학습한 동영상 목록 조회 성공하였습니다.");
        responseDTO.setData(videos);
        return responseDTO;
    }
}
