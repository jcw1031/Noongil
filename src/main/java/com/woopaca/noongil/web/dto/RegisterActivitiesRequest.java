package com.woopaca.noongil.web.dto;

import java.time.LocalDate;

public record RegisterActivitiesRequest(LocalDate activityDate, int stepsCount) {
}
