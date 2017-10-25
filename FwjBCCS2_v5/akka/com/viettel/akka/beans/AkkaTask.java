package com.viettel.akka.beans;

import java.io.Serializable;

/**
 * Payload container
 */
public class AkkaTask implements Serializable {


    private Object bean;
    private String serviceName;
    private Class className;

    private String methodName;
    private Object[] parameters;

    public AkkaTask(Object bean, String methodName, Object... parameters) {
        this.bean = bean;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public AkkaTask(String serviceName, String methodName, Object... parameters) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public AkkaTask(Class className, String methodName, Object... parameters) {
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
