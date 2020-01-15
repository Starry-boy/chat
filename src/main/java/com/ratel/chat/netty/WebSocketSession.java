package com.ratel.chat.netty;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description webSocketSession 用户存放websocket 链接 和用户信息
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class WebSocketSession {
    private Channel channel;
    private String name;
    private Long id;
    private Date loginTime;
    private Date logoutTime;
}
