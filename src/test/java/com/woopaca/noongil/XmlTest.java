package com.woopaca.noongil;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@SpringBootTest
public class XmlTest {

    @Value("${public-data.service-key}")
    private String serviceKey;

    @Autowired
    private RestClient restClient;

    @Test
    void test() {
        LocalDate currentDate = LocalDate.now();
        URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                .queryParam("serviceKey", serviceKey)
                .queryParam("solYear", currentDate.getYear())
                .build(true)
                .toUri();
        log.info("uri: {}", uri);

        Response response = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .body(Response.class);
        log.info("response: {}", response);
    }

    @JacksonXmlRootElement(localName = "response")
    record Response(
            @JacksonXmlProperty(localName = "body") Body body
    ) {
    }

    record Body(
            @JacksonXmlProperty(localName = "items") Items items
    ) {
    }

    record Items(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "item") List<HolidayItem> itemList
    ) {
    }

    record HolidayItem(
            @JacksonXmlProperty(localName = "isHoliday") String isHoliday,
            @JacksonXmlProperty(localName = "dateName") String dateName,
            @JacksonXmlProperty(localName = "locdate") String locdate
    ) {
    }
}
