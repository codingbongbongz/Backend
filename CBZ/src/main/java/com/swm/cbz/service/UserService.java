package com.swm.cbz.service;

import com.swm.cbz.domain.User;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class UserService {



    public Optional<User> searchUserById(String userId) {
        return userRepository.findById(userId);
    }

}
