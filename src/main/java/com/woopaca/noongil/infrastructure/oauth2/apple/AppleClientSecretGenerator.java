package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.woopaca.noongil.infrastructure.apple.AppleProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class AppleClientSecretGenerator {

    private final AppleProperties appleProperties;

    private ECPrivateKey privateKey;

    public AppleClientSecretGenerator(AppleProperties appleProperties) {
        this.appleProperties = appleProperties;
    }

    @PostConstruct
    public void loadKey() {
        try (InputStream resourceAsStream = new ClassPathResource(appleProperties.getPrivateKeyPath())
                .getInputStream()) {
            String key = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder()
                    .decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            this.privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("private key(.p8) 파일을 읽는 중 오류가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    public String generateClientSecret() {
        Algorithm algorithm = Algorithm.ECDSA256(null, privateKey);
        Date currentDate = new Date();
        return JWT.create()
                .withKeyId(appleProperties.getPrivateKeyId())
                .withIssuer(appleProperties.getTeamId())
                .withIssuedAt(currentDate)
                .withExpiresAt(currentDate.toInstant().plusSeconds(60))
                .withAudience("https://appleid.apple.com")
                .withSubject(appleProperties.getBundleId())
                .sign(algorithm);
    }
}
