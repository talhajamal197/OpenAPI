package com.anue.openapi.demo.auth;

import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private static final String USERNAME = "talha";
    private static final String PASSWORD = "1234";
    private static final long TOKEN_VALIDITY_DURATION_MILLISECONDS = 5 * 60 * 1000; // 5 minutes

    private Map<String, LocalDateTime> tokenStore = new HashMap<>();

    public String authenticate(String username, String password) {
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            String token = generateToken(username,password);
            tokenStore.put(token, LocalDateTime.now());
            return token;
        }
        return null;
    }

    public boolean validateToken(String token) {
        return token.equals(generateToken(USERNAME,PASSWORD));
    }

    private String generateToken(String username, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
