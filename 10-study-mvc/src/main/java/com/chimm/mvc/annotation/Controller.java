package com.chimm.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
@Target(ElementType.TYPE) //对类注解
@Retention(RetentionPolicy.RUNTIME) //保存到jvm中
@Documented
public @interface Controller {

    /**
     * 表示给Controller注册别名
     */
    String value() default "";
}
