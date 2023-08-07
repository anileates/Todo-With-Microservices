package com.aea.authservice.service;

import com.aea.authservice.dto.request.RegistrationRequest;
import com.aea.authservice.model.User;
import com.aea.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(RegistrationRequest registrationRequest) {
        User user = User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public User updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean enableUser(String email) {
        int i = userRepository.enableUser(email);

        return i > 0 ? true : false;
    }

    public Optional<User> getOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getOneByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
