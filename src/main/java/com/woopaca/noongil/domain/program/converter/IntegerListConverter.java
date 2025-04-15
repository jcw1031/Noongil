package com.woopaca.noongil.domain.program.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }

        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) {
            return Collections.emptyList();
        }

        return Arrays.stream(dbData.split(","))
                .map(Integer::valueOf)
                .toList();
    }
}
