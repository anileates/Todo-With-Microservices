package com.aea.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String APP_SECRET;

    @Value("${jwt.expiresIn}")
    private long EXPIRES_IN;

    public String generateJwtToken(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(null)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        /**
         * Claims, JWT'deki alanlardir
         */
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
    }
}
