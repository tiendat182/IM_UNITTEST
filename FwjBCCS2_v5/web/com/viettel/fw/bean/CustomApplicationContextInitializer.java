package com.viettel.fw.bean;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.IOException;

/**
 * @author nhannt34
 * Config de load cau hinh chay WS hay DIRECT
 */
public class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

    @Override
    public void initialize(ConfigurableWebApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        try {
            environment.getPropertySources().addFirst(new ResourcePropertySource("systemConfig.properties"));
        } catch (IOException ignore) {

        }
    }
}