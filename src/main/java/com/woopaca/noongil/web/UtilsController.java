package com.woopaca.noongil.web;

import com.woopaca.noongil.adapter.holiday.PublicHolidayDeterminer;
import com.woopaca.noongil.web.dto.ApiResults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/utils")
public class UtilsController {

    private final PublicHolidayDeterminer publicHolidayDeterminer;

    public UtilsController(PublicHolidayDeterminer publicHolidayDeterminer) {
        this.publicHolidayDeterminer = publicHolidayDeterminer;
    }

    @GetMapping("/holidays")
    public ApiResults.ApiResponse<Boolean> isHoliday(@RequestParam("date") LocalDate date) {
        boolean publicHoliday = publicHolidayDeterminer.isPublicHoliday(date);
        return ApiResults.success(publicHoliday);
    }
}
