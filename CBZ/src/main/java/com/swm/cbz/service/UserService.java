package com.swm.cbz.service;

import com.swm.cbz.domain.User;
import com.swm.cbz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> searchUserById(String userId) {
        return userRepository.findById(userId);
    }

}
