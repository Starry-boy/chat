package com.ratel.chat.config;

import com.ratel.chat.handler.WebSocketHandler;
import com.ratel.chat.netty.manager.NettyManager;
import com.ratel.chat.properties.NettyProperties;
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
import org.springframework.beans.factory.annotation.Qualifier;
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
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfig {

   /* @Bean
    public WebSocketHandler initWebSocketHandler(){
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        log.info("------------------------------< init WebSocketHandler  >----------------------------");
        return webSocketHandler;
    }*/

    /**
     * @return
     * @Description 启动netty
     * @Date 上午11:57 2020/1/14
     * @Author ratel
     * @Param
     **/
    @Bean
    public NettyManager initNetty(NettyProperties nettyProperties,WebSocketHandler webSocketHandler) throws Exception {
        NettyManager nettyManager = new NettyManager();
        ServerBootstrap bootstrap = nettyManager.getBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        log.info("------------------------------< chat.port:{} chat.maxContentLength:{} chat.messagePersistence:{} >----------------------------",nettyProperties.getPort(),nettyProperties.getMaxContentLength(),nettyProperties.isMessagePersistence());
        try {
            bootstrap.group(workGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(nettyProperties.getPort())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(nettyProperties.getMaxContentLength()));
                            socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                            socketChannel.pipeline().addLast(webSocketHandler);
                        }
                    });
            log.info("------------------------------< init ServerBootstrap >----------------------------");
            ChannelFuture cf = bootstrap.bind(nettyProperties.getPort()).sync(); // 服务器异步创建绑定
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync(); // 释放线程池资源
            workGroup.shutdownGracefully().sync();
        }
        return nettyManager;
    }
}
