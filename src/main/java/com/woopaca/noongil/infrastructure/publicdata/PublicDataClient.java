package com.woopaca.noongil.infrastructure.publicdata;

import com.woopaca.noongil.infrastructure.publicdata.CultureEventsResponse.CultureEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(ProgramResponse.class);
        } catch (Exception e) {
            log.warn("[PublicDataClient][getLiveAlonePrograms] 공공데이터 API 호출 에러. page: {}, pageSize: {}", page, pageSize, e);
            return ProgramResponse.empty();
        }
    }

    public int getTotalPage() {
        return getTotalPage(DEFAULT_PAGE_SIZE);
    }

    public int getTotalPage(int pageSize) {
        ProgramResponse liveAlonePrograms = getLiveAlonePrograms(1, 1);
        int totalCount = liveAlonePrograms.getTotalCount();
        if (totalCount == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    private URI generateLiveAloneProgramsUri(int page, int pageSize) {
        int startRow = (page - 1) * pageSize + 1;
        int endRow = startRow + pageSize - 1;
        return UriComponentsBuilder.fromUriString("http://openapi.seoul.go.kr:8088/{authentication-key}/json/tbPartcptn/{start-row}/{end-row}")
                .buildAndExpand(publicDataAuthenticationKey, startRow, endRow)
                .toUri();
    }

    @Cacheable(cacheNames = "cultureEvents", key = "#page", unless = "#result == null || #result.isEmpty()")
    public List<CultureEvent> getCultureEvents(int page) {
        int startRow = (page - 1) * 50 + 1;
        int endRow = startRow + 50 - 1;
        URI uri = UriComponentsBuilder.fromUriString("http://openapi.seoul.go.kr:8088/{authentication-key}/json/culturalEventInfo/{start-row}/{end-row}")
                .buildAndExpand(publicDataAuthenticationKey, startRow, endRow)
                .toUri();

        try {
            return Optional.ofNullable(restClient.get()
                            .uri(uri)
                            .retrieve()
                            .body(CultureEventsResponse.class))
                    .map(CultureEventsResponse::getCultureEvents)
                    .orElseGet(Collections::emptyList);
        } catch (Exception e) {
            log.warn("[PublicDataClient][getCultureEvents] 공공데이터 API 호출 에러. page: {}", page, e);
            return Collections.emptyList();
        }
    }

    /**
     * 1시간마다 문화행사 캐시 삭제
     */
    @CacheEvict(cacheNames = "cultureEvents", allEntries = true)
    @Scheduled(fixedRate = 60 * 60 * 1_000)
    public void evictCultureEventsCache() {
        log.info("[PublicDataClient][evictCultureEventsCache] 문화행사 캐시 삭제");
    }
}
