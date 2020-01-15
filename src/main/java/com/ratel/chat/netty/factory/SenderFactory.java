package com.ratel.chat.netty.factory;

import com.ratel.chat.netty.sender.MessageSender;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Component
public class SenderFactory {

    private static final Map<String, MessageSender> messageSenderMap = new HashMap<>();

    public MessageSender getMessageSender(String type) {
        return messageSenderMap.get(type);
    }

    public static void registrationMessageSender(String type,MessageSender messageSender){
        messageSenderMap.put(type,messageSender);
    }
}
