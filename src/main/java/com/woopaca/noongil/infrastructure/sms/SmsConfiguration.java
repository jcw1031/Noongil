package com.woopaca.noongil.infrastructure.sms;

import lombok.Getter;
import lombok.Setter;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfiguration {

    private String apiKey;
    private String secretKey;
    private String senderNumber;

    @Bean
    public DefaultMessageService messageService() {
        return new DefaultMessageService(apiKey, secretKey, "https://api.coolsms.co.kr");
    }
}
