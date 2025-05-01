package com.woopaca.noongil.domain.mlmodel;

import com.woopaca.noongil.infrastructure.config.AWSConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Component
//@Profile("!local")
public class S3HealthModelStorage implements HealthModelStorage {

    private static final String HEALTH_MODEL_S3_PREFIX = "models/";

    private final S3Client s3Client;
    private final AWSConfiguration awsConfiguration;

    public S3HealthModelStorage(S3Client s3Client, AWSConfiguration awsConfiguration) {
        this.s3Client = s3Client;
        this.awsConfiguration = awsConfiguration;
    }

    @Override
    public String store(HealthModelFile healthModelFile) {
        String fileName = healthModelFile.getFileName();
        String key = HEALTH_MODEL_S3_PREFIX + fileName;
        try (healthModelFile) {
            InputStream inputStream = healthModelFile.getInputStream();
            PutObjectRequest request = generateRequest(key);
            RequestBody requestBody = RequestBody.fromInputStream(inputStream, inputStream.available());
            s3Client.putObject(request, requestBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String cloudFrontDomain = awsConfiguration.getCloudFrontDomain();
        return String.join("/", cloudFrontDomain, key);
    }

    private PutObjectRequest generateRequest(String key) {
        return PutObjectRequest.builder()
                .bucket(awsConfiguration.getS3Bucket())
                .key(key)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .build();
    }

    @Override
    public void delete(String modelName) {
        String key = HEALTH_MODEL_S3_PREFIX + modelName + HealthModelFile.EXTENSION;
        s3Client.deleteObject(builder -> builder.bucket(awsConfiguration.getS3Bucket()).key(key));
    }
}
