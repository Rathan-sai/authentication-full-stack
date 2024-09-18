package com.tech.programming.Authentication_server.config;

import com.tech.programming.Authentication_server.service.JWTService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class SecretKeyRotationScheduler {

    @Autowired
    JWTService jwtService;

    private String primaryKey;
    @Getter
    private final List<String> previousKeys = new ArrayList<>();

    public void rotateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        storeKey(encodedKey, LocalDateTime.now());
    }

    public void storeKey(String key, LocalDateTime rotationDate) {
        if (primaryKey != null) {
            previousKeys.add(primaryKey);
        }
        primaryKey = key;

        System.out.println("New primary key stored. Previous keys: " + previousKeys);
    }
}
