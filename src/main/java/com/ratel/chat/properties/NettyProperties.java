package com.ratel.chat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description netty 配置
 * @Author ratel
 * @Date 2020/1/14
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "chat")
public class NettyProperties {
    /** netty 端口 */
    private int port = 9001;
    /** netty 内容最大长度 */
    private int maxContentLength = 100000;
    /** 消息持久化 */
    private boolean messagePersistence = false;
    /** 消息是否加密 */
    private boolean encryption = false;
    /** 是否需要授权才能使用 */
    private boolean needAuthorization = false;
}
