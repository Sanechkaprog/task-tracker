package com.alexkrug.backend.security;

import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BaseUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public BaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NullMarked
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with given username not found"));
        return new UserSecurity(user);
    }
}
