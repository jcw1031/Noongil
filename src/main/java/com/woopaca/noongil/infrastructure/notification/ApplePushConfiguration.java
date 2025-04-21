package com.woopaca.noongil.infrastructure.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.woopaca.noongil.infrastructure.apple.AppleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class ApplePushConfiguration {

    @Bean
    @Profile({"production", "develop", "local-develop"})
    public ApnsClient apnsClient(AppleProperties appleProperties) {
        try (InputStream resourceAsStream = new ClassPathResource(appleProperties.getPrivateKeyPath())
                .getInputStream()) {
            ApnsSigningKey signingKey = ApnsSigningKey
                    .loadFromInputStream(resourceAsStream, appleProperties.getTeamId(), appleProperties.getPrivateKeyId());
            return new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                    .setSigningKey(signingKey)
                    .build();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("APNs 클라이언트를 생성하는 중 오류가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }
}
