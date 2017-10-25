package com.viettel.bccs.fw.logging;

import com.viettel.web.common.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * @author nhannt34
 * @since 13/02/2017
 */
@Component
public class KpiBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = Logger.getLogger("KPI_LOGGER");

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] declaredFields = bean.getClass().getDeclaredMethods();
        Optional<Method> kpiBean = Arrays.stream(declaredFields)
                .filter(x -> x.getAnnotation(Kpi.class) != null)
                .findFirst();
        if (kpiBean.isPresent()) {
            try {
                // annotation @Kpi se chi dung trong class BaseController, neu khong se bao loi
                BaseController base = (BaseController) bean;
                base.setUuid(UUID.randomUUID().toString());
                base.setLastRun(Instant.now().toEpochMilli());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;  // you can return any other object as well
    }
}

