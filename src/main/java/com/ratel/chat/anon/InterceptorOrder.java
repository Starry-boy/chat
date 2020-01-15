package com.ratel.chat.anon;

import com.sun.istack.internal.NotNull;

import java.lang.annotation.*;

/**
 * @Description 拦截器执行顺序注解
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InterceptorOrder {

    int value() ;
}
