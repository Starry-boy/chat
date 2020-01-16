package com.ratel.chat.netty.manager;

import io.netty.bootstrap.ServerBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Component
public class NettyManger {
    @Autowired
    private ServerBootstrap serverBootstrap;
}
