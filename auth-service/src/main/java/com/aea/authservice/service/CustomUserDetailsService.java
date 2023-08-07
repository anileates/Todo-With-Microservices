package com.aea.authservice.service;

import com.aea.authservice.model.User;
import com.aea.authservice.repository.UserRepository;
import com.aea.authservice.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();

        return CustomUserDetails.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).get();
        return CustomUserDetails.create(user);
    }
}
