package com.woopaca.noongil.web.config;

import com.woopaca.noongil.adapter.auth.AuthenticateInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticateInterceptor authenticateInterceptor;

    public WebMvcConfiguration(AuthenticateInterceptor authenticateInterceptor) {
        this.authenticateInterceptor = authenticateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticateInterceptor);
    }
}
