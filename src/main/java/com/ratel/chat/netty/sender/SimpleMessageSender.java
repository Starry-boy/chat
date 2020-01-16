package com.ratel.chat.netty.sender;

import com.ratel.chat.entity.Message;
import com.ratel.chat.interceptor.WebSocketInterceptor;
import com.ratel.chat.netty.WebSocketSession;
import com.ratel.chat.netty.manager.WebSocketSessionManger;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Log4j2
//@Component
//@ConditionalOnMissingBean(AbstractMessageSender.class)
public class SimpleMessageSender extends AbstractMessageSender {
    private final String TYPE = "simple";
    @Override
    public void sendMsg(Message message) {
        log.debug("----------------------------->>>   Demo SimpleMessageSender ");
        Long to = message.getTo();
        WebSocketSession session = WebSocketSessionManger.getSessionById(to);
        this.baseSendMessage(session,message);
    }

    @Override
    public String messageType() {
        return TYPE;
    }
}
