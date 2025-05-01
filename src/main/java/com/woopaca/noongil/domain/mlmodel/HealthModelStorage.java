package com.woopaca.noongil.domain.mlmodel;

public interface HealthModelStorage {

    String store(HealthModelFile healthModelFile);

    void delete(String modelName);
}
