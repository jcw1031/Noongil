package com.woopaca.noongil.application.mlmodel;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.mlmodel.HealthModel;
import com.woopaca.noongil.domain.mlmodel.HealthModelFile;
import com.woopaca.noongil.domain.mlmodel.HealthModelRepository;
import com.woopaca.noongil.domain.mlmodel.HealthModelStorage;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.event.ModelUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class HealthModelService {

    private final UserRepository userRepository;
    private final HealthModelStorage healthModelStorage;
    private final HealthModelRepository healthModelRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UniqueNameGenerator uniqueNameGenerator;

    public HealthModelService(UserRepository userRepository, HealthModelStorage healthModelStorage, HealthModelRepository healthModelRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.healthModelStorage = healthModelStorage;
        this.healthModelRepository = healthModelRepository;
        this.eventPublisher = eventPublisher;
        this.uniqueNameGenerator = new UniqueNameGenerator();
    }

    @Transactional
    public String updateHealthModel(Long userId, MultipartFile multipartFile) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        String email = user.getEmail();
        String modelName = uniqueNameGenerator.generate(email);
        HealthModelFile healthModelFile = HealthModelFile.of(multipartFile, modelName);

        healthModelRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        existingModel -> {
                            eventPublisher.publishEvent(new ModelUpdateEvent(existingModel.getModelName()));
                            existingModel.changeModelName(modelName);
                        },
                        () -> {
                            HealthModel healthModel = HealthModel.builder()
                                    .modelName(modelName)
                                    .userId(user.getId())
                                    .build();
                            healthModelRepository.save(healthModel);
                        }
                );

        return healthModelStorage.store(healthModelFile);
    }

    public HealthModel findModel() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        return healthModelRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자의 AI 모델이 존재하지 않습니다."));
    }

    @Async
    @TransactionalEventListener
    protected void deleteModel(ModelUpdateEvent event) {
        log.info("모델 업데이트 이벤트 처리");
        String modelName = event.modelName();
        healthModelStorage.delete(modelName);
    }
}
