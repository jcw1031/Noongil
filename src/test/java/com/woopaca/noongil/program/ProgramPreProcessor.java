package com.woopaca.noongil.program;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class ProgramPreProcessor {

    @Autowired
    private RestClient restClient;

    @TestConfiguration
    static class TestRestClientConfiguration {

        @Bean
        public RestClient restClient(FactoryBean factoryBean) {
            JdkClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.jdk()
                    .withCustomizer(customizeFactory ->
                            customizeFactory.setReadTimeout(Duration.ofMillis(3_000L)))
                    .build();

            return RestClient.builder()
                    .requestFactory(requestFactory)
                    .build();
        }
    }

    @Test
    void collectAndProcessProgramsData() {
        URI uri = UriComponentsBuilder.fromUriString("http://openapi.seoul.go.kr:8088/6d71787a696a637737346c597a6968/json/tbPartcptn/{startIndex}/{endIndex}")
                .queryParam("auth", "6d71787a696a637737346c597a6968")
                .buildAndExpand(0, 10)
                .toUri();
        List<Map<String, String>> programs = restClient.get()
                .uri(uri)
                .retrieve()
                .body(ProgramResponse.class)
                .getPrograms();
        log.info("programs: {}", programs);
    }

    record ProgramResponse(@JsonAlias("tbPartcptn") Programs programs) {

        public List<Map<String, String>> getPrograms() {
            return this.programs.row;
        }
    }

    record Programs(List<Map<String, String>> row) {
    }
}
