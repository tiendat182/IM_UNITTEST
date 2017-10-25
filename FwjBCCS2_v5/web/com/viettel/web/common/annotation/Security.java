package com.viettel.web.common.annotation;

/**
 * Created by thiendn1 on 4/1/2015.
 */
//x_1604_1: dung giai phap phan quyen cua SpringSecurity => ko can tu viet

//x_1604_1 @Retention(RetentionPolicy.RUNTIME)
//x_1604_1 pha @Target(ElementType.METHOD) //can use in method only.
public @interface Security {
    String value() default "";
}