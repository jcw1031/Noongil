package com.woopaca.noongil.application.mlmodel;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.mlmodel.HealthModel;
import com.woopaca.noongil.domain.mlmodel.HealthModelFile;
import com.woopaca.noongil.domain.mlmodel.HealthModelRepository;
import com.woopaca.noongil.domain.mlmodel.HealthModelStorage;
import com.woopaca.noongil.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HealthModelService {

    private final HealthModelStorage healthModelStorage;
    private final UniqueNameGenerator uniqueNameGenerator;
    private final HealthModelRepository healthModelRepository;

    public HealthModelService(HealthModelStorage healthModelStorage, HealthModelRepository healthModelRepository) {
        this.healthModelStorage = healthModelStorage;
        this.uniqueNameGenerator = new UniqueNameGenerator();
        this.healthModelRepository = healthModelRepository;
    }

    @Transactional
    public String updateHealthModel(MultipartFile multipartFile) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        String email = authenticatedUser.getEmail();
        String modelName = uniqueNameGenerator.generate(email);
        HealthModelFile healthModelFile = HealthModelFile.of(multipartFile, modelName);

        healthModelRepository.findByUserId(authenticatedUser.getId())
                .ifPresentOrElse(
                        existingModel -> {
                            existingModel.changeModelName(modelName);
                        },
                        () -> {
                            HealthModel healthModel = HealthModel.builder()
                                    .modelName(modelName)
                                    .userId(authenticatedUser.getId())
                                    .build();
                            healthModelRepository.save(healthModel);
                        }
                );
        return healthModelStorage.store(healthModelFile);
    }
}
