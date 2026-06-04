package com.Senai.Filmes.Security;


import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtUtil {

    @Value("${jwt.secrets}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

