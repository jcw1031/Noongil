package com.woopaca.noongil.adapter.holiday;

import com.woopaca.noongil.infrastructure.publicdata.PublicHolidayClient;
import com.woopaca.noongil.infrastructure.publicdata.PublicHolidaysResponse.HolidayItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PublicHolidayDeterminer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PublicHolidayClient publicHolidayClient;

    public PublicHolidayDeterminer(PublicHolidayClient publicHolidayClient) {
        this.publicHolidayClient = publicHolidayClient;
    }

    public boolean isPublicHoliday(LocalDate date) {
        if (date == null) {
            return false;
        }

        List<HolidayItem> publicHolidays = publicHolidayClient.getPublicHolidays(date.getYear(), date.getMonthValue());
        return publicHolidays.stream()
                .map(HolidayItem::locdate)
                .map(DATE_FORMATTER::parse)
                .anyMatch(date::equals);
    }
}
