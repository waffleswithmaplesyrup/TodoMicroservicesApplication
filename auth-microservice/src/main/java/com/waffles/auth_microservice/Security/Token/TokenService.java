package com.waffles.auth_microservice.Security.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenService {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String retrieveToken(HttpServletRequest request) {
        String authorizationToken = request.getHeader("Authorization");

        if(authorizationToken == null || !authorizationToken.startsWith("Bearer")) throw new RuntimeException("Token required");

        return authorizationToken.substring(7);
    }

    /// Generate Token
    public String generateToken(String email) {

        Map<String, Object> claims = new HashMap<>();

        Instant now = Instant.now();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(10, ChronoUnit.MINUTES)))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes) ;
    }

    /// Decode email from Token
    public String decodeToken(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }

    /// extracts info from claims
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimResolver.apply(claims);
    }

    /// check for token validity based on token expiry
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiresAt = extractClaim(token, claims -> claims.getExpiration());
        return expiresAt.before(new Date());
    }
}
