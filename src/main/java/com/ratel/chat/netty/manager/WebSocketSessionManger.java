package com.ratel.chat.netty.manager;

import com.ratel.chat.netty.WebSocketSession;
import com.ratel.chat.netty.factory.WebSocketSessionFactory;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Log4j2
public class WebSocketSessionManger {
    private static final Map<Long, WebSocketSession> webSocketSessionMap = new HashMap<>();
    private static final Map<String, WebSocketSession> webSocketSessionNameMap = new HashMap<>();

    public static WebSocketSession getSessionById(Long sessionId){
        return webSocketSessionMap.get(sessionId);
    }

    public static WebSocketSession getSessionByName(String sessionName){
        return webSocketSessionNameMap.get(sessionName);
    }

    public static void createSession(Long sessionId, ChannelHandlerContext ctx){
        log.debug("----------------------------->>>   createSession: name={} id={} ",ctx.name(),sessionId);
        WebSocketSession webSocketSession = WebSocketSessionFactory.createWebSocketSession(sessionId,ctx);
        webSocketSessionMap.put(sessionId, webSocketSession);
        webSocketSessionNameMap.put(ctx.name(),webSocketSession);
    }

    public static void destroySession(String sessionName){
        WebSocketSession webSocketSession = webSocketSessionNameMap.get(sessionName);
        log.debug("----------------------------->>>   destroySession: name={} id={} ",webSocketSession.getName(),webSocketSession.getId());
        webSocketSessionMap.remove(webSocketSession.getId());
        webSocketSessionNameMap.remove(sessionName);
    }
}
