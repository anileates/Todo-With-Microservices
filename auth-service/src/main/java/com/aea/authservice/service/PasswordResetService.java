package com.aea.authservice.service;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aea.authservice.common.TokenUtils;
import com.aea.authservice.model.PasswordResetToken;
import com.aea.authservice.model.User;
import com.aea.authservice.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createToken(User user) {
        PasswordResetToken confirmationToken = new PasswordResetToken(
                TokenUtils.getSHA256Token(),
                LocalDateTime.now().plusHours(12),
                user
        );

        return tokenRepository.save(confirmationToken);
    }

    public Optional<PasswordResetToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }
}