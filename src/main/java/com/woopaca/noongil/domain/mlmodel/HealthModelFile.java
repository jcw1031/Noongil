package com.woopaca.noongil.domain.mlmodel;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class HealthModelFile implements Closeable {

    private static final String EXTENSION = ".mlmodel";

    private final String fileName;
    private final InputStream inputStream;

    public HealthModelFile(String healthModelName, InputStream inputStream) {
        this.fileName = healthModelName + EXTENSION;
        this.inputStream = inputStream;
    }

    public static HealthModelFile of(MultipartFile multipartFile, String healthModelName) {
        try {
            return new HealthModelFile(healthModelName, multipartFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
