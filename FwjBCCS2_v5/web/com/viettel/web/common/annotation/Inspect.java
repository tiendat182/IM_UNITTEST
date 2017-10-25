package com.viettel.web.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by thiendn1 on 4/1/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface Inspect {
    String updateArea() default "";
    String successMessage() default "";
    String errorMessage() default "";
    boolean ignoreNotification() default false;
    boolean ignoreSuccessMsg() default false;
    boolean validationFailed() default false;
    boolean noLogging() default false;

}