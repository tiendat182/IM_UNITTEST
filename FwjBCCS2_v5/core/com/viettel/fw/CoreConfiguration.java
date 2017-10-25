package com.viettel.fw;

import com.viettel.fw.bean.ApplicationContextProvider;
import com.viettel.web.log.LoggingWithoutSession;
import com.viettel.ws.common.utils.InjectGenericWebInfoInterceptor;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptorExtension;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;


/**
 * Created by thiendn1 on 7/12/2015.
 */
@Configuration
public class CoreConfiguration implements ApplicationContextAware {

    @Autowired
    LoggingWithoutSession loggingWithoutSession;

    @Bean
    public InjectGenericWebInfoInterceptor soapInterceptor() {
        return new InjectGenericWebInfoInterceptor();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

//    @Bean
//    public MBeanExporter mBeanExporter() {
//        MBeanExporter exporter = new MBeanExporter();
//        Map<String, Object> mbeanMap = Maps.newHashMap();
//        mbeanMap.put("BCCS2:category=statistics,name=ehcache", managementService());
//        exporter.setBeans(mbeanMap);
//        exporter.afterPropertiesSet();
//        return exporter;
//    }
}