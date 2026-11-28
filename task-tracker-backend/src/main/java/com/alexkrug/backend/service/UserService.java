package com.alexkrug.backend.service;

import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.response.TaskDto;
import com.alexkrug.backend.kafka.EmailKafkaProducer;
import com.alexkrug.backend.mapper.ServiceMapper;
import com.alexkrug.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ServiceMapper serviceMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailKafkaProducer kafkaProducer;

    public boolean isUserRegistered(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.isPresent();
    }

    public User getUser(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User saveUser(String username, String password, String email) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, email);
        User savedUser = userRepository.save(user);
        log.info("USER SAVED");
        kafkaProducer.sendWelcomeEmailTo(savedUser.getEmail());
        return savedUser;
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
