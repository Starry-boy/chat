package com.ratel.chat.handler;

import com.ratel.chat.entity.Message;
import com.ratel.chat.netty.WebSocketSession;
import com.ratel.chat.netty.manager.WebSocketSessionManger;
import com.ratel.chat.properties.ChatProperties;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 鉴权处理器
 * @Author ratel
 * @Date 2020/1/16
 * @Version 1.0
 */
@Log4j2
@ChannelHandler.Sharable
public class PermissionWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private ChatProperties chatProperties;

    @Autowired(required = false)
    private AuthorizationHandler authorizationHandler;

    @Autowired
    private MessageForwardHandler messageForwardHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        channelHandlerContext.fireChannelRead(textWebSocketFrame);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(">>>  webSocket 连接异常：{}", cause);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            if (!StringUtils.isEmpty(uri)) {
                Map<String, String> map = this.parseUrl(uri);
                // 获取用户token
                String token = map.get(chatProperties.getTokenName());
                //如果请求 url 不对 直接抛出异常
                if (chatProperties.isNeedAuthorization()) {
                    //校验token
                    Assert.notNull(authorizationHandler, "授权处理器不能为空");
                    if (!authorizationHandler.authorization(token)) {
                        ctx.close();
                    }
                    WebSocketSession session = WebSocketSessionManger.getSessionByName(ctx.name());
                    if (session != null) {
                        messageForwardHandler.redirectMessage(authorizationHandler.loginWelcome(session.getId()));
                    }
                }
                request.setUri(chatProperties.getWebSocketUri());
                ctx.fireChannelRead(request.retain());
            }
        }
        ctx.fireChannelRead(msg);
    }

    /**
     * @return
     * @Description 解析url参数
     * @Date 上午10:03 2020/1/16
     * @Author ratel
     * @Param
     **/
    private Map<String, String> parseUrl(String url) {
        Map<String, String> map = new HashMap<>();
        if (url == null) {
            return map;
        }
        url = url.trim();
        if (url.equals("")) {
            return map;
        }
        String[] urlParts = url.split("\\?");
        String baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return map;
        }
        //有参数
        String[] params = urlParts[1].split("&");
//        entity.params = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
