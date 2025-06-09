package org.e2e.labe2e04.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.key}")
    private String secret;

    private Algorithm getSigningAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String extractUsername(String token) {
        return JWT.require(getSigningAlgorithm())
                .build()
                .verify(token)
                .getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .sign(getSigningAlgorithm());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

}
