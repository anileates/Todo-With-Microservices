package com.aea.authservice.common;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.google.common.hash.Hashing;

/*
 * This class is used to generate random tokens for email verification and password reset
 */
public class TokenUtils {
    public static String getSHA256Token() {
        String randomText = UUID.randomUUID().toString();

        return Hashing.sha256().hashString(randomText, StandardCharsets.UTF_8)
                .toString();
    }
}
