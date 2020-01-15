package com.ratel.chat.interceptor;

import com.ratel.chat.entity.Message;

/**
 * @Description 抽象的拦截器
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public interface WebSocketInterceptor {
    /**
     * @return
     * @Description 接收消息 前置处理方法
     * @Date        上午9:36 2020/1/15
     * @Author      ratel
     * @Param
     **/
     void receiveBefore(Message message);


  /*  *//**
     * @return
     * @Description 接收消息后置处理方法
     * @Date        上午9:36 2020/1/15
     * @Author      ratel
     * @Param
     **//*
    void receiveAfter(Message message);*/

    /**
     * @return
     * @Description 发送信息前置处理方法
     * @Date        上午9:38 2020/1/15
     * @Author      ratel
     * @Param
     **/
    void sendBefore(Message message);

    /**
     * @return
     * @Description 发送信息后置处理方法
     * @Date        上午9:39 2020/1/15
     * @Author      ratel
     * @Param
     **/
    void sendAfter(Message message);

}
