package com.woopaca.noongil.infrastructure.publicdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record CultureEventsResponse(@JsonProperty("culturalEventInfo") CultureEvents cultureEvents) {

    public static List<CultureEvent> empty() {
        return Collections.emptyList();
    }

    public List<CultureEvent> getCultureEvents() {
        if (this.cultureEvents == null) {
            return Collections.emptyList();
        }
        return this.cultureEvents.cultureEvents();
    }

    public record CultureEvents(@JsonProperty("row") List<CultureEvent> cultureEvents) {
    }

    public record CultureEvent(@JsonProperty("TITLE") String title,
                               @JsonProperty("HMPG_ADDR") String url,
                               @JsonDeserialize(using = CultureEventsEndDateDeserializer.class)
                               @JsonProperty("END_DATE") LocalDate endDate) {
    }
}
