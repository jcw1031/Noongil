package com.woopaca.noongil.web;

import com.woopaca.noongil.application.activity.ActivityService;
import com.woopaca.noongil.application.mlmodel.HealthModelService;
import com.woopaca.noongil.domain.mlmodel.HealthModel;
import com.woopaca.noongil.domain.mlmodel.HealthModelFile;
import com.woopaca.noongil.web.dto.ActivityModelResponse;
import com.woopaca.noongil.web.dto.ApiResults;
import com.woopaca.noongil.web.dto.ApiResults.ApiResponse;
import com.woopaca.noongil.web.dto.RegisterActivitiesRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final HealthModelService healthModelService;

    @Value("${aws.cloud-front-domain}")
    private String cloudFrontDomain;

    @Value("${secret.key}")
    private String secretKey;

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
    public ApiResponse<String> uploadModel(@PathVariable("userId") Long userId,
                                           @RequestPart("model") MultipartFile modelFile,
                                           @RequestPart("secret") String secret) {
        if (!StringUtils.hasText(secret) || !secret.equals(secretKey)) {
            throw new IllegalArgumentException("비밀키가 일치하지 않습니다.");
        }

        String modelUri = healthModelService.updateHealthModel(userId, modelFile);
        return ApiResults.success("모델이 업로드 되었습니다.", modelUri);
    }

    @GetMapping("/models")
    public ApiResponse<ActivityModelResponse> getModelInfo() {
        HealthModel model = healthModelService.findModel();
        LocalDate updatedAt = model.getUpdatedAt().toLocalDate();
        String url = String.join("/", cloudFrontDomain, "models/" + model.getModelName() + HealthModelFile.EXTENSION);
        ActivityModelResponse response = new ActivityModelResponse(updatedAt, url);
        return ApiResults.success(response);
    }
}
