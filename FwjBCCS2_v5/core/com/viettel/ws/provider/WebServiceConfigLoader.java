package com.viettel.ws.provider;

import com.viettel.ws.common.utils.WsRequestCreator;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.beanutils.BeanDeclaration;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.commons.configuration.beanutils.XMLBeanDeclaration;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thiendn1 on 1/16/2015.
 */
public class WebServiceConfigLoader {

    CombinedConfiguration configuration;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, WsTemplate>> wsTemplateMap = new ConcurrentHashMap<>();

    private static WebServiceConfigLoader instance;

    static {
        instance = new WebServiceConfigLoader();
    }

    public WebServiceConfigLoader() {
        configuration = new CombinedConfiguration();
    }


    public static WebServiceConfigLoader getInstance() {
        return instance;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, WsTemplate>> getWsTemplateMap() {
        return wsTemplateMap;
    }


    public WsRequestCreator getWsConfig(String key) throws Exception {
        BeanDeclaration decl = new XMLBeanDeclaration(configuration, key);
        return (WsRequestCreator) BeanHelper.createBean(decl);
    }

    public WsRequestCreator getWsConfigOperator(String key) throws Exception {
        String wsKey = configuration.getString(key + ".url");
        String serviceMethod = configuration.getString(key + ".service");
        BeanDeclaration decl = new XMLBeanDeclaration(configuration, wsKey);
        WsRequestCreator ws = (WsRequestCreator) BeanHelper.createBean(decl);
        ws.setServiceName(serviceMethod);
        return ws;
    }

}
