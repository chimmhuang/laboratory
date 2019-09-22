package com.chimm.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
@Target(ElementType.TYPE) //对类注解
@Retention(RetentionPolicy.RUNTIME) //保存到jvm中
@Documented
public @interface Service {

    /**
     * 表示给Service注册别名
     */
    String value() default "";
}
