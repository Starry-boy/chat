package com.ratel.chat.netty.sender;

import com.alibaba.fastjson.JSON;
import com.ratel.chat.entity.Message;
import com.ratel.chat.handler.EncryptionHandler;
import com.ratel.chat.netty.WebSocketSession;
import com.ratel.chat.netty.factory.SenderFactory;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public abstract class AbstractMessageSender implements MessageSender{
    @Autowired(required = false)
    private EncryptionHandler encryptionHandler;
    @Value("${chat.encryption}")
    private boolean isEncryption;

    public void baseSendMessage(WebSocketSession webSocketSession, Message message){
        Long from = message.getFrom();
        Long to = message.getTo();
        message.setTo(from);
        message.setFrom(to);
        //消息加密
        if (isEncryption) {
            Assert.notNull(encryptionHandler,"配置了信息加密 需要手写一个加密处理器 实现 EncryptionHandler");
            encryptionHandler.decrypt(message);
        }
        webSocketSession.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
    }

    @PostConstruct
    public void init(){
        SenderFactory.registrationMessageSender(this.messageType(),this);
    }
}
