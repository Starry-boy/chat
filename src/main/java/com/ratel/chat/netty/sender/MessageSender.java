package com.ratel.chat.netty.sender;

import com.ratel.chat.entity.Message;

/**
 * @Description TODO
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public interface MessageSender {

    void sendMsg(Message message);

    String messageType();
}
