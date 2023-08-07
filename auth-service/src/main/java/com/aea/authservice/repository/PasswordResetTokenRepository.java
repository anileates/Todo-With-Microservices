package com.aea.authservice.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aea.authservice.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
