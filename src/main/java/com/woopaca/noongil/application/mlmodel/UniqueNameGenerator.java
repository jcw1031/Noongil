package com.woopaca.noongil.application.mlmodel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

public class UniqueNameGenerator {

    private static final MessageDigest DIGEST;

    static {
        try {
            DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generate(String identifier) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String input = String.join("_", identifier, currentDateTime.toString());
        byte[] hash = DIGEST.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(hash)
                .substring(0, 16);
    }
}
