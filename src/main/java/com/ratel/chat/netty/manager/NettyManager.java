package com.ratel.chat.netty.manager;

import io.netty.bootstrap.ServerBootstrap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @Description netty Manager
 * @Author ratel
 * @Date 2020/1/14
 * @Version 1.0
 */
@Log4j2
@Data
public class NettyManager {
    ServerBootstrap bootstrap = new ServerBootstrap();

    public void sendMsg(){

    }
}
