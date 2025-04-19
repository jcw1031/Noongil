package com.woopaca.noongil.infrastructure.publicdata;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ProgramDto(Map<String, String> program) {

    private static final String UNIQUE_ID_KEY = "PARTCPTN_ID";
    private static final String NAME_KEY = "PARTCPTN_SJ";
    private static final String RECEPTION_START_DATE_KEY = "RCEPT_DE1";
    private static final String RECEPTION_END_DATE_KEY = "RCEPT_DE2";
    private static final String PROGRAM_START_DATE_KEY = "PROGRS_DE1";
    private static final String PROGRAM_END_DATE_KEY = "PROGRS_DE2";
    private static final String BOROUGH_KEY = "ATDRC_NM";
    private static final String AGE_RANGE_KEY = "AGRDE_NM";
    private static final String GENDER_KEY = "SEXDSTN_NM";
    private static final String FEE_TYPE_KEY = "PARTCPT_CT_NM";
    private static final String FEE_AMOUNT_KEY = "PARTCPT_AMOUNT";
    private static final String RECEPTION_METHOD_KEY = "RCEPT_MTH_NM";
    private static final String RECEPTION_URL_KEY = "RCEPT_MTH_LINK";
    private static final String CONTACT_KEY = "PROGRS_INQRY";
    private static final String ADDRESS_KEY = "PLACE_ADRES1";
    private static final String DETAILED_ADDRESS_KEY = "PLACE_ADRES2";
    private static final String INSTITUTION_KEY = "INSTT_NM";

    public String getUniqueId() {
        return program.get(UNIQUE_ID_KEY);
    }

    public String getName() {
        return program.get(NAME_KEY);
    }

    public LocalDate getReceptionStartDate() {
        String registrationStartDate = program.get(RECEPTION_START_DATE_KEY);
        return parseDate(registrationStartDate);
    }

    public LocalDate getReceptionEndDate() {
        String registrationEndDate = program.get(RECEPTION_END_DATE_KEY);
        return parseDate(registrationEndDate);
    }

    public LocalDate getProgramStartDate() {
        String programStartDate = program.get(PROGRAM_START_DATE_KEY);
        return parseDate(programStartDate);
    }

    public LocalDate getProgramEndDate() {
        String programEndDate = program.get(PROGRAM_END_DATE_KEY);
        return parseDate(programEndDate);
    }

    private LocalDate parseDate(String registrationStartDate) {
        if (StringUtils.hasText(registrationStartDate)) {
            return LocalDate.parse(registrationStartDate);
        }
        return null;
    }

    public String getBorough() {
        return program.get(BOROUGH_KEY);
    }

    public List<Integer> getAgeRange() {
        String ageRangeText = program.get(AGE_RANGE_KEY);
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(ageRangeText);

        Set<Integer> ages = new HashSet<>();
        while (matcher.find()) {
            ages.add(Integer.parseInt(matcher.group()));
        }
        if (ageRangeText.contains("60대 이상")) {
            ages.add(100);
        }

        if (ages.isEmpty()) {
            return Collections.emptyList();
        }

        return ages.stream()
                .sorted()
                .toList();
    }

    public String getGender() {
        return program.get(GENDER_KEY);
    }

    public String getFeeType() {
        return program.get(FEE_TYPE_KEY);
    }

    public String getFeeAmount() {
        String feeAmount = program.get(FEE_AMOUNT_KEY);
        return StringUtils.hasText(feeAmount) ? feeAmount : null;
    }

    public String getReceptionMethod() {
        return program.get(RECEPTION_METHOD_KEY);
    }

    public String getReceptionUrl() {
        String receptionUrl = program.get(RECEPTION_URL_KEY);
        return StringUtils.hasText(receptionUrl) ? receptionUrl : null;
    }

    public String getContact() {
        String contact = program.get(CONTACT_KEY);
        return StringUtils.hasText(contact) ? contact : null;
    }

    public String getSimpleAddress() {
        return program.get(ADDRESS_KEY);
    }

    public String getDetailedAddress() {
        return program.get(DETAILED_ADDRESS_KEY);
    }

    public String getInstitution() {
        return program.get(INSTITUTION_KEY);
    }

    public String getFullAddress() {
        String address = program.get(ADDRESS_KEY);
        String detailedAddress = program.get(DETAILED_ADDRESS_KEY);

        if (StringUtils.hasText(detailedAddress)) {
            return String.join(" ", address, detailedAddress);
        }
        return address;
    }

    @Override
    public String toString() {
        return "ProgramDto{" +
               "uniqueId='" + getUniqueId() + '\'' +
               ", name='" + getName() + '\'' +
               ", registrationStartDate=" + getReceptionStartDate() +
               ", registrationEndDate=" + getReceptionEndDate() +
               ", programStartDate=" + getProgramStartDate() +
               ", programEndDate=" + getProgramEndDate() +
               ", borough='" + getBorough() + '\'' +
               ", ageRange='" + getAgeRange() + '\'' +
               ", gender='" + getGender() + '\'' +
               ", feeType='" + getFeeType() + '\'' +
               ", feeAmount='" + getFeeAmount() + '\'' +
               ", receptionMethod='" + getReceptionMethod() + '\'' +
               ", receptionUrl='" + getReceptionUrl() + '\'' +
               ", contact='" + getContact() + '\'' +
               ", simpleAddress='" + getSimpleAddress() + '\'' +
               ", detailedAddress='" + getDetailedAddress() + '\'' +
               ", institution='" + getInstitution() + '\'' +
               ", fullAddress='" + getFullAddress() + '\'' +
               '}';
    }
}
