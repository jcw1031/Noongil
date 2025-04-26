package com.woopaca.noongil.application.mlmodel;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.mlmodel.HealthModelFile;
import com.woopaca.noongil.domain.mlmodel.HealthModelStorage;
import com.woopaca.noongil.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HealthModelService {

    private final HealthModelStorage healthModelStorage;
    private final UniqueNameGenerator uniqueNameGenerator;

    public HealthModelService(HealthModelStorage healthModelStorage) {
        this.healthModelStorage = healthModelStorage;
        this.uniqueNameGenerator = new UniqueNameGenerator();
    }

    @Transactional
    public String updateHealthModel(MultipartFile multipartFile) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        String email = authenticatedUser.getEmail();
        String modelName = uniqueNameGenerator.generate(email);
        HealthModelFile healthModelFile = HealthModelFile.of(multipartFile, modelName);

        // TODO: DB에 모델 정보 업데이트
        return healthModelStorage.store(healthModelFile);
    }
}
