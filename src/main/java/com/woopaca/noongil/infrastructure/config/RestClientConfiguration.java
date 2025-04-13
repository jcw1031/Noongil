package com.woopaca.noongil.infrastructure.config;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient restClient() {
        JdkClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.jdk()
                .withCustomizer(customizeFactory ->
                        customizeFactory.setReadTimeout(Duration.ofMillis(10_000L)))
                .build();

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}
