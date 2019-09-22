package com.chimm.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
@Target(ElementType.FIELD) //对字段注解
@Retention(RetentionPolicy.RUNTIME) //保存到jvm中
@Documented
public @interface Autowired {

    /**
     * 指定要注入类的ioc名称
     */
    String value() default "";
}
