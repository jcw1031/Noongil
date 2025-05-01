package com.woopaca.noongil.web.dto;

import java.time.LocalDate;

public record ActivityModelResponse(LocalDate updatedAt, String modelUrl) {
}
