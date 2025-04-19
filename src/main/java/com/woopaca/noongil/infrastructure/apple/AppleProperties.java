package com.woopaca.noongil.infrastructure.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "apple")
public class AppleProperties {

    private String clientId;
    private String teamId;
    private String privateKeyId;
    private String privateKeyPath;
}
