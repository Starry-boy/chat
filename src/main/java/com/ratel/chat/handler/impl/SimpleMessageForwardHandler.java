package com.ratel.chat.handler.impl;

import com.ratel.chat.entity.Message;
import com.ratel.chat.handler.MessageForwardHandler;
import com.ratel.chat.netty.factory.SenderFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import com.ratel.chat.netty.sender.MessageSender;

/**
 * @Description 默认消息转发处理器
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Log4j2
//@Component
public class SimpleMessageForwardHandler implements MessageForwardHandler {
    @Autowired
    private SenderFactory senderFactory;

    @Override
    public void redirectMessage(Message message) {
        log.debug(">>>   用户 {} 于 {} 向 {} 发送信息, 信息类型 {}",
                message.getFrom(), message.getSendDate(), message.getTo(), message.getType()
        );
        String type = message.getType();
        MessageSender messageSender = senderFactory.getMessageSender(type);
        messageSender.sendMsg(message);
    }
}
