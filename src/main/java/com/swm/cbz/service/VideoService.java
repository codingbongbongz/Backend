package com.swm.cbz.service;

import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class VideoService {
    private VideoRepository videoRepository;
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TranscriptService transcriptService;

    public ResponseEntity<Video> uploadVideo(String username, String link){
        System.out.println(username);
        System.out.println(link);
        Optional<Users> userOptional = userService.searchUserById(username);
        if(!userOptional.isPresent()){
            System.out.println("error");
            throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
        }
        System.out.println("here");
        return transcriptService.fetchTranscripts(link, username);
    }

}
