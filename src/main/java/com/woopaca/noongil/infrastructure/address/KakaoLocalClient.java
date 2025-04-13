package com.woopaca.noongil.infrastructure.address;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
public class KakaoLocalClient {

    private final RestClient restClient;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public KakaoLocalClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public AddressResponse addressToCoordinate(String address) {
        URI uri = UriComponentsBuilder.fromUriString("https://dapi.kakao.com/v2/local/search/address")
                .queryParam("query", address)
                .build()
                .toUri();

        try {
            return restClient.get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
                    .retrieve()
                    .body(AddressResponse.class);
        } catch (Exception e) {
            log.warn("[KakaoLocalClient][addressToCoordinate] 주소 좌표 변환 API 호출 에러. address: {}", address, e);
            return null;
        }
    }

    public record AddressResponse(@JsonAlias("documents") List<Address> addresses) {
    }

    public record Address(String x, String y) {
    }
}
