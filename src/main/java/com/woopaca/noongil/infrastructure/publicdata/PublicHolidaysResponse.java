package com.woopaca.noongil.infrastructure.publicdata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@JacksonXmlRootElement(localName = "response")
public record PublicHolidaysResponse(@JacksonXmlProperty(localName = "body") Body body) {

    public List<HolidayItem> getHolidays() {
        if (body == null || body.items() == null || body.items().itemList() == null) {
            return Collections.emptyList();
        }
        return body.items().itemList();
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

    public record HolidayItem(
            @JacksonXmlProperty(localName = "isHoliday") String isHoliday,
            @JacksonXmlProperty(localName = "dateName") String dateName,
            @JacksonXmlProperty(localName = "locdate") String locdate
    ) implements Serializable {
    }
}
