package com.woopaca.noongil.infrastructure.publicdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class PublicDataClient {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private final RestClient restClient;

    @Value("${public-data.authentication-key}")
    private String publicDataAuthenticationKey;

    public PublicDataClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ProgramResponse getLiveAlonePrograms(int page) {
        return getLiveAlonePrograms(page, DEFAULT_PAGE_SIZE);
    }

    public ProgramResponse getLiveAlonePrograms(int page, int pageSize) {
        if (page < 1) {
            throw new IllegalArgumentException("page는 1 이상이어야 합니다. page: " + page);
        }

        URI uri = generateLiveAloneProgramsUri(page, pageSize);
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(ProgramResponse.class);
    }

    private URI generateLiveAloneProgramsUri(int page, int pageSize) {
        int startRow = (page - 1) * pageSize + 1;
        int endRow = startRow + pageSize - 1;
        return UriComponentsBuilder.fromUriString("http://openapi.seoul.go.kr:8088/{authentication-key}/json/tbPartcptn/{start-row}/{end-row}")
                .buildAndExpand(publicDataAuthenticationKey, startRow, endRow)
                .toUri();
    }
}
