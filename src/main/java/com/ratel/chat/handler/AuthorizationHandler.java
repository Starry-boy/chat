package com.ratel.chat.handler;

import com.ratel.chat.entity.Message;

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

    /**
     * @return
     * @Description 登录欢迎语句
     * @Date        上午10:42 2020/1/16
     * @Author      ratel
     * @Param
     **/
    Message loginWelcome(Long id);
}
