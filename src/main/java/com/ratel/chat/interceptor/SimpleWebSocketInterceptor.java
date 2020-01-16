package com.ratel.chat.interceptor;

import com.ratel.chat.anon.InterceptorOrder;
import com.ratel.chat.entity.Message;
import com.ratel.chat.handler.MessageForwardHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @Description 默认websocket 消息拦截器 将消息
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Log4j2
@InterceptorOrder(1)
public class SimpleWebSocketInterceptor implements WebSocketInterceptor {

    @Override
    public void receiveBefore(Message message) {
        log.debug(">>>   Demo receiveBefore ");
    }

    @Override
    public void sendBefore(Message message) {
        log.debug(">>>   Demo sendBefore ");
    }

    @Override
    public void sendAfter(Message message) {
        log.debug(">>>   Demo sendAfter ");
    }
}
