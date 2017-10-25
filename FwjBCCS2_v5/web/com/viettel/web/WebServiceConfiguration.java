package com.viettel.web;

import com.viettel.fw.logging.PerformanceProxy;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by thiendn1 on 27/1/2016.
 */
public class WebServiceConfiguration {
    private static final Logger logger = Logger.getLogger(WebServiceConfiguration.class);

    @Autowired
    CxfWsClientFactory cxfWsClientFactory;

    @Autowired
    ApplicationContext context;

    private String prefix;
    private String suffix;
    private List<String> interfaces;
    private List<String> excludeInterfaces;
    private Map<String, String> extendedInterfaces;
    private Map<String, String> specificInterfaces;


    @PostConstruct
    public void init() {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
        if (excludeInterfaces != null) {
            interfaces.removeAll(excludeInterfaces);
        }

        for (String name : interfaces) {
            try {
                Class clzz = Class.forName(name.trim());
                beanFactory.registerSingleton(prefix + clzz.getSimpleName() + suffix, PerformanceProxy.newInstance(clzz.getSimpleName(), cxfWsClientFactory.createWsClient(clzz)));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        for (String name : extendedInterfaces.keySet()) {
            try {
                Class clzz = Class.forName(extendedInterfaces.get(name.trim()));
                beanFactory.registerSingleton(name.trim(), cxfWsClientFactory.createWsClient(clzz));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (!DataUtil.isNullOrEmpty(specificInterfaces)) {
            for (String name : specificInterfaces.keySet()) {
                try {
                    Class clzz = Class.forName(specificInterfaces.get(name.trim()));
                    beanFactory.registerSingleton(DataUtil.lowerFirstChar(name.trim()), cxfWsClientFactory.createWsClient(clzz, name));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void getInvoiceManagementWs() {
        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(""))
                    .filterInputsBy(new FilterBuilder().includePackage("com.viettel.bccs.sale.service"))
                    .setScanners(
                            new SubTypesScanner(false),
                            new TypeAnnotationsScanner()));
            Set<Class<?>> annotated = reflections.getSubTypesOf(Object.class);
            for (Class clzz : annotated) {
                if (clzz.isInterface()) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public List<String> getExcludeInterfaces() {
        return excludeInterfaces;
    }

    public void setExcludeInterfaces(List<String> excludeInterfaces) {
        this.excludeInterfaces = excludeInterfaces;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Map<String, String> getExtendedInterfaces() {
        return extendedInterfaces;
    }

    public void setExtendedInterfaces(Map<String, String> extendedInterfaces) {
        this.extendedInterfaces = extendedInterfaces;
    }

    public Map<String, String> getSpecificInterfaces() {
        return specificInterfaces;
    }

    public void setSpecificInterfaces(Map<String, String> specificInterfaces) {
        this.specificInterfaces = specificInterfaces;
    }
}

