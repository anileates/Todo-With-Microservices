package com.aea.apigateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String APP_SECRET;

    @Value("${jwt.expiresIn}")
    private long EXPIRES_IN;


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
