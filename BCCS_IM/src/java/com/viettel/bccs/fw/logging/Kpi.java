package com.viettel.bccs.fw.logging;

import java.lang.annotation.*;
import java.util.Date;

/**
 * @author nhannt34
 * @since 13/02/2017
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Kpi {
    String value() default "";
}
