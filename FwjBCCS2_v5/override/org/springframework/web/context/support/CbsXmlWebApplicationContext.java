package org.springframework.web.context.support;

/**
 * Created by Lamnv5 on 2/16/2017.
 */

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Created by PM2-LAMNV5 on 2/16/2017.
 */

public class CbsXmlWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory factory = super.createBeanFactory();
        factory.setAllowBeanDefinitionOverriding(false);
        return factory;
    }

}

