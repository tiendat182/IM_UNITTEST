package com.viettel.fw.bean;

import org.springframework.context.ApplicationContext;

/**
 * Created by khuongdv on 5/8/2015.
 */
public class ApplicationContextProvider {
    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
}