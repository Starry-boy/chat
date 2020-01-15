package com.ratel.chat.handler;

import com.ratel.chat.entity.Message;

/**
 * @Description 消息转发处理器
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public interface MessageForwardHandler {

    /**
     * @return
     * @Description 消息转发
     * @Date        上午10:47 2020/1/15
     * @Author      ratel
     * @Param
     **/
    void redirectMessage(Message message);

}
