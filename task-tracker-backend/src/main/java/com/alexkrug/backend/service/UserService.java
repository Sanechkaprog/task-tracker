package com.alexkrug.backend.service;

import com.alexkrug.backend.domain.User;
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
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.isPresent();
    }

    public Optional<User> findUser(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User saveUser(String username, String password) {
        User user = new User(username, password);
        return userRepository.save(user);

    }
}
