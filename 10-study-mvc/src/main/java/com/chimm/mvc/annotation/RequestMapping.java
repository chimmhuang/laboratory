package com.chimm.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
@Target({ElementType.TYPE,ElementType.METHOD}) //对类注解
@Retention(RetentionPolicy.RUNTIME) //保存到jvm中
@Documented
public @interface RequestMapping {

    /**
     * url映射
     */
    String value() default "";
}
