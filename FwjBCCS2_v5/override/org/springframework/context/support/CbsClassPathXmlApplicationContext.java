package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Created by Lamnv5 on 2/16/2017.
 */
public class CbsClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {
    public CbsClassPathXmlApplicationContext() {
    }

    public CbsClassPathXmlApplicationContext(String configLocation) throws BeansException {
        super(configLocation);
    }

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory factory = super.createBeanFactory();
        factory.setAllowBeanDefinitionOverriding(false);

        return factory;
    }
}
