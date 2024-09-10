package com.startrac.task_management_system.service;

import com.startrac.task_management_system.model.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.message}")
    private String message;

    @Override
    public String generateToken(UserData userData) {
        Key secretKey = getSecretKey();
        return Jwts.builder().subject(userData.getUsername()).issuedAt(new Date()).signWith(secretKey).compact();
/*        Map<String, String> jwtTokenGenerated = new HashMap<>();
        jwtTokenGenerated.put("token", jwsToken);
        jwtTokenGenerated.put("message", message);
        return jwtTokenGenerated;*/
    }

    @Override
    public boolean validateToken(String jwsToken, UserDetails userDetails) {
        try {
            SecretKey secretKey = getSecretKey();
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwsToken);
        } catch (JwtException e) {
            return false;
        }
        final String username = extractUserName(jwsToken);
        return (userDetails.getUsername().equals(username));
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey secretKey = getSecretKey();
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}

