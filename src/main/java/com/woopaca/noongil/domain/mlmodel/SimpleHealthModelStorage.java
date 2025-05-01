package com.woopaca.noongil.domain.mlmodel;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//@Component
//@Profile("local")
public class SimpleHealthModelStorage implements HealthModelStorage {

    @Override
    public String store(HealthModelFile healthModelFile) {
        return "";
    }

    @Override
    public void delete(String modelName) {

    }
}
