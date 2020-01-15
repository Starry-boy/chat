package com.ratel.chat.netty.factory;

import com.ratel.chat.netty.WebSocketSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * @Description TODO
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public class WebSocketSessionFactory {

    public static WebSocketSession createWebSocketSession(Long sessionId, ChannelHandlerContext ctx){
        return new WebSocketSession().setChannel(ctx.channel())
                .setLoginTime(new Date()).setName(ctx.name())
                .setId(sessionId);
    }
}
