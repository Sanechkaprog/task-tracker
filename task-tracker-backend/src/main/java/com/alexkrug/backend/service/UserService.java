package com.alexkrug.backend.service;

import com.alexkrug.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserRegistered(String username) {
        return Optional
                .ofNullable(userRepository.findUserByUsername(username))
                .isPresent();
    }
}
