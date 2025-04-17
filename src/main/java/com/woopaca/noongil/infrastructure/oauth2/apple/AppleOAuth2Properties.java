package com.woopaca.noongil.infrastructure.oauth2.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth2.apple")
public class AppleOAuth2Properties {

    private String clientId;
    private String teamId;
    private String privateKeyId;
    private String privateKeyPath;
}
