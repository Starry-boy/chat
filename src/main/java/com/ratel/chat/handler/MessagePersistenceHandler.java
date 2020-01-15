package com.ratel.chat.handler;

import com.ratel.chat.entity.Message;

/**
 * @Description 消息持久化处理器
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public interface MessagePersistenceHandler {

    /**
     * @return
     * @Description 接收信息
     * @Date        上午10:32 2020/1/15
     * @Author      ratel
     * @Param
     **/
    void receiveMessage(Message message);

}
