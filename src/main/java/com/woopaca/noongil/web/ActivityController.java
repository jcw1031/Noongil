package com.woopaca.noongil.web;

import com.woopaca.noongil.application.activity.ActivityService;
import com.woopaca.noongil.application.mlmodel.HealthModelService;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.RegisterActivitiesRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final HealthModelService healthModelService;

    public ActivityController(ActivityService activityService, HealthModelService healthModelService) {
        this.activityService = activityService;
        this.healthModelService = healthModelService;
    }

    @PostMapping
    public ApiResponse<Void> registerActivities(@RequestBody List<RegisterActivitiesRequest> activities) {
        activityService.registerActivities(activities);
        return ApiResults.success("활동 데이터가 업데이트 되었습니다.", null);
    }

    @PostMapping("/{userId}/models")
    public ApiResponse<String> uploadModel(@PathVariable("userId") String userId,
                                           @RequestPart("model") MultipartFile modelFile) {
        String modelUri = healthModelService.updateHealthModel(modelFile);
        return ApiResults.success("모델이 업로드 되었습니다.", modelUri);
    }
}
