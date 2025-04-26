package com.woopaca.noongil.infrastructure.publicdata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CultureEventsEndDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateTimeString = p.getText();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, FORMATTER);
        return dateTime.toLocalDate();
    }
}