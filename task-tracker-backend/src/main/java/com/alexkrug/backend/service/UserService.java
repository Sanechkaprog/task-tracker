package com.alexkrug.backend.service;

import com.alexkrug.backend.domain.Task;
import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.TaskDto;
import com.alexkrug.backend.mapper.ServiceMapper;
import com.alexkrug.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ServiceMapper serviceMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean isUserRegistered(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.isPresent();
    }

    public Optional<User> findUser(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User getUser(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User saveUser(String username, String password, String email) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, email);
        return userRepository.save(user);

    }

    public boolean isEmailExist(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.isPresent();
    }

    public List<TaskDto> getUserTasks(String username) {
        User user =  userRepository.getByUsernameWithTasks(username);
        return user.getTasks()
                .stream()
                .map(serviceMapper::toTaskDto)
                .toList();

    }
}
