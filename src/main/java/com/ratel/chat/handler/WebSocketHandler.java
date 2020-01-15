package com.ratel.chat.handler;

import com.alibaba.fastjson.JSON;
import com.ratel.chat.anon.InterceptorOrder;
import com.ratel.chat.entity.Message;
import com.ratel.chat.interceptor.WebSocketInterceptor;
import com.ratel.chat.netty.manager.WebSocketSessionManger;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * * @Description webSocket 处理器
 * * @Author ratel
 *
 * @Date 2020/1/14
 * @Version 1.0
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired(required = false)
    private MessagePersistenceHandler persistenceHandler;

    @Autowired
    private MessageForwardHandler messageForwardHandler;

    private List<WebSocketInterceptor> interceptors;

    @Value("${chat.message-persistence}")
    private boolean isPersistence;

    @Value("${chat.encryption}")
    private boolean isEncryption;

    @Value("${chat.needAuthorization}")
    private boolean needAuthorization;

    @Autowired(required = false)
    private EncryptionHandler encryptionHandler;

    @Autowired(required = false)
    private AuthorizationHandler authorizationHandler;

    @Autowired
    private void initInterceptors(List<WebSocketInterceptor> webSocketInterceptors) {
        //初始化 拦截器执行顺序
        if (!CollectionUtils.isEmpty(webSocketInterceptors)) {
            List<WebSocketInterceptor> noAnonList = new ArrayList<>();
            Iterator<WebSocketInterceptor> iterator = webSocketInterceptors.iterator();
            while (iterator.hasNext()) {
                WebSocketInterceptor next = iterator.next();
                InterceptorOrder annotation = next.getClass().getAnnotation(InterceptorOrder.class);
                if (annotation != null) {
                    break;
                }
                noAnonList.add(next);
                iterator.remove();
            }
            //优先排序有注解的
            webSocketInterceptors.sort((o1, o2) ->
                    {
                        int i = o1.getClass().getAnnotation(InterceptorOrder.class).value();
                        int j = o2.getClass().getAnnotation(InterceptorOrder.class).value();
                        return -(i - j);
                    }
            );
            //再排序无注解的
            noAnonList.sort((o1, o2) -> {
                char i = o1.getClass().getSimpleName().charAt(0);
                char j = o2.getClass().getSimpleName().charAt(0);
                return (-i - j);
            });
            webSocketInterceptors.addAll(noAnonList);
            this.interceptors = webSocketInterceptors;
        }
        log.info("------------------------------< init WebSocketInterceptor start!! >----------------------------");
        for (int i = 0; i < this.interceptors.size(); i++) {
            log.info("------------------------------< init WebSocketInterceptor:{} order:{} >----------------------------", this.interceptors.get(i).getClass().getSimpleName(), i);
        }
        log.info("------------------------------< init WebSocketInterceptor end!! >----------------------------");
    }

    /**
     * @return
     * @Description 客户端建立连接
     * @Date 下午2:09 2020/1/14
     * @Author ratel
     * @Param
     **/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加到channelGroup通道组
        WebSocketSessionManger.createSession(new Random().nextLong(), ctx);
        log.debug("------------------------------< channelActive >----------------------------");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("------------------------------< channelRegistered >----------------------------");
        super.channelRegistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("------------------------------< handlerAdded >----------------------------");
        super.handlerAdded(ctx);
    }


    /**
     * @return
     * @Description 与客户端断开链接
     * @Date 下午2:09 2020/1/14
     * @Author ratel
     * @Param
     **/
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("------------------------------< channelInactive >----------------------------");
        //销毁回话
        WebSocketSessionManger.destroySession(ctx.name());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("------------------------------< channelUnregistered >----------------------------");
        super.channelUnregistered(ctx);
    }

    /**
     * @return
     * @Description 读取消息
     * @Date 下午2:10 2020/1/14
     * @Author ratel
     * @Param
     **/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg && !(msg instanceof FullHttpRequest)) super.channelRead(ctx, msg);
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        //如果url包含参数，需要处理
        if (!uri.contains("?")) super.channelRead(ctx, msg);
        String substring = uri.substring(uri.indexOf('?') + 1);
        String[] split = substring.split("=");
        String token = null;
        for (int i = 0; i < substring.length(); i++) {
            if (!split[i].equals("token")) continue;
            if (++i > split.length - 1) continue;
            token = split[split.length - 1];
            break;
        }
        //校验token
        if (needAuthorization) {
            if (authorizationHandler.authorization(token)) {
                ctx.channel().close();
            }
            super.channelRead(ctx, msg);
        }

        /*}else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" +frame.text());
            sendAllMessage(frame.text());
        }
        ctx.writeAndFlush("测试");
        super.channelRead(ctx, msg);*/
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String json = textWebSocketFrame.text();
        Message message = null;
        try {
            message = JSON.parseObject(json, Message.class);
        } catch (Exception e) {
            log.error("------------------------------< Message DeSerializable Error >----------------------------");
            log.error(e);
            return;
        }
        //消息解密
        if (isEncryption) {
            Assert.notNull(encryptionHandler, "配置了信息加密 需要手写一个解密处理器 实现 EncryptionHandler");
            encryptionHandler.decrypt(message);
        }
        //TODO 使用阻塞队列 消息持久化
        if (isPersistence) {
            Assert.notNull(encryptionHandler, "配置了信息持久化 需要手写一个信息持久化处理器 实现 MessagePersistenceHandler");
            persistenceHandler.receiveMessage(message);
        }
        //无拦截器 直接将 消息转发给用户
        if (CollectionUtils.isEmpty(interceptors)) {
            messageForwardHandler.redirectMessage(message);
            return;
        }
        //读消息前
        for (int i = 0; i < interceptors.size(); i++) {
            WebSocketInterceptor webSocketInterceptor = interceptors.get(i);
            webSocketInterceptor.receiveBefore(message);
        }
        //发送消息前
        for (int i = 0; i < interceptors.size(); i++) {
            WebSocketInterceptor webSocketInterceptor = interceptors.get(i);
            webSocketInterceptor.sendBefore(message);
        }
        //发送消息
        messageForwardHandler.redirectMessage(message);
        //发送消息后
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            WebSocketInterceptor webSocketInterceptor = interceptors.get(i);
            webSocketInterceptor.receiveBefore(message);
            webSocketInterceptor.sendAfter(message);
        }
    }
}
