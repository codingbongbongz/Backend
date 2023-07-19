package com.swm.cbz.service;

import com.swm.cbz.domain.User;
import com.swm.cbz.domain.Video;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {
    private VideoRepository videoRepository;

    private UserRepository userRepository;

    private UserService userService;

    public ResponseEntity<Video> uploadVideo(String username, String link){
        Optional<User> userOptional = userService.searchUserById(username);
        if(userOptional.isPresent()){
            Video video = new Video();
            video.setLink(link);

        }
    }

}
