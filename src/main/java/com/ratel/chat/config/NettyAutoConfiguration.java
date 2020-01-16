package com.ratel.chat.config;

import com.ratel.chat.handler.MessageForwardHandler;
import com.ratel.chat.handler.WebSocketHandler;
import com.ratel.chat.handler.impl.SimpleMessageForwardHandler;
import com.ratel.chat.interceptor.SimpleWebSocketInterceptor;
import com.ratel.chat.interceptor.WebSocketInterceptor;
import com.ratel.chat.netty.sender.MessageSender;
import com.ratel.chat.netty.sender.SimpleMessageSender;
import com.ratel.chat.properties.ChatProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description netty 初始化配置
 * @Author
 * @Date
 * @Version
 */
@Log4j2
@Configuration
@EnableConfigurationProperties(ChatProperties.class)
public class NettyAutoConfiguration {

   /* @Bean
    public WebSocketHandler initWebSocketHandler(){
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        log.info("----------------------------->>>   init WebSocketHandler  ");
        return webSocketHandler;
    }*/
   @Bean
   @ConditionalOnMissingBean(MessageForwardHandler.class)
    public MessageForwardHandler initDefaultMessageForwardHandler(){
        MessageForwardHandler simpleMessageForwardHandler = new SimpleMessageForwardHandler();
        return simpleMessageForwardHandler;
    }

    @Bean
    @ConditionalOnMissingBean(WebSocketInterceptor.class)
    public WebSocketInterceptor initDefaultWebSocketInterceptor(){
        WebSocketInterceptor simpleWebSocketInterceptor = new SimpleWebSocketInterceptor();
        return simpleWebSocketInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean(MessageSender.class)
    public MessageSender initDefaultMessageSender(){
        MessageSender messageSender =  new SimpleMessageSender();
        return messageSender;
    }
    /**
     * @return
     * @Description 启动netty
     * @Date 上午11:57 2020/1/14
     * @Author ratel
     * @Param
     **/
    @Bean
    public ServerBootstrap initServerBootstrap(ChatProperties chatProperties, WebSocketHandler webSocketHandler) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        log.info("----------------------------->>>   chat.port:{} chat.maxContentLength:{} chat.messagePersistence:{} ", chatProperties.getPort(), chatProperties.getMaxContentLength(), chatProperties.isMessagePersistence());
        try {
            bootstrap.group(workGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(chatProperties.getPort())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(chatProperties.getMaxContentLength()));
                            socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler(chatProperties.getWebSocketUri()));
                            socketChannel.pipeline().addLast(webSocketHandler);
                        }
                    });
            log.info("----------------------------->>>   init ServerBootstrap ");
            ChannelFuture cf = bootstrap.bind(chatProperties.getPort()).sync(); // 服务器异步创建绑定
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync(); // 释放线程池资源
            workGroup.shutdownGracefully().sync();
        }
        return bootstrap;
    }
}
