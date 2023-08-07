package com.aea.authservice.service;

import com.aea.authservice.common.TokenUtils;
import com.aea.authservice.model.EmailVerificationToken;
import com.aea.authservice.model.User;
import com.aea.authservice.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private final EmailTokenRepository tokenRepository;

    public EmailVerificationToken createToken(User user) {
        EmailVerificationToken confirmationToken = new EmailVerificationToken(
                TokenUtils.getSHA256Token(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(12),
                user
        );

        return tokenRepository.save(confirmationToken);
    }

    public Optional<EmailVerificationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public boolean setConfirmedAt(String token) {
        int i = tokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    
        return i > 0 ? true : false;
    }
}
