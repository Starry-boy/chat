package com.ratel.chat.handler;

/**
 * @Description 权限校验处理器
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public interface AuthorizationHandler {

    /**
     * @return
     * @Description 权限校验
     * @Date        下午4:26 2020/1/15
     * @Author      ratel
     * @Param
     **/
    boolean authorization(String token);
}
