package com.woopaca.noongil.infrastructure.publicdata;

import com.woopaca.noongil.infrastructure.publicdata.PublicHolidaysResponse.HolidayItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class PublicHolidayClient {

    private final RestClient restClient;

    @Value("${public-data.service-key}")
    private String serviceKey;

    public PublicHolidayClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Cacheable(cacheNames = "publicHolidays", key = "#year + '-' + #month")
    public List<HolidayItem> getPublicHolidays(int year, int month) {
        String solMonth = month < 10 ? "0" + month : String.valueOf(month);
        URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                .queryParam("serviceKey", serviceKey)
                .queryParam("solYear", year)
                .queryParam("solMonth", solMonth)
                .build(true)
                .toUri();

        PublicHolidaysResponse response = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .body(PublicHolidaysResponse.class);
        if (response == null) {
            return Collections.emptyList();
        }
        return response.getHolidays();
    }
}
