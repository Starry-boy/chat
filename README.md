<h1 align="center"><a href="https://github.com/jxc19960306/chat" target="_blank">Chat</a></h1>
> chat 是个 java 简单的聊天框架。
<p align="center">
<a href="#"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8-yellow.svg?style=flat-square"/></a>


## 简介

chat 是个 java 简单的聊天框架。

ps：群聊 需要 重写`com.ratel.chat.netty.sender.MessageSender`

## 预留接口

#### 加密处理器
实现接口 `com.ratel.chat.handler.EncryptionHandler`
配置 `com.ratel.chat.properties.NettyProperties#encryption`
默认实现 `无`
#### 登录认证处理器
实现接口 `com.ratel.chat.handler.AuthorizationHandler`
配置 `com.ratel.chat.properties.NettyProperties#needAuthorization`
默认实现 `无`
#### 信息持久化处理器
实现接口 `com.ratel.chat.handler.MessagePersistenceHandler`
配置 `com.ratel.chat.properties.NettyProperties#messagePersistence`
默认实现 `无`
### 消息转发处理器
实现接口 `com.ratel.chat.handler.MessageForwardHandler`
配置 `无`
默认实现 `com.ratel.chat.handler.impl.SimpleMessageForwardHandler`
### 消息拦截器
实现接口 `com.ratel.chat.interceptor.WebSocketInterceptor`
配置 `无`
默认实现 `com.ratel.chat.interceptor.SimpleWebSocketInterceptor`

### 消息发送者
实现接口 `com.ratel.chat.netty.sender.MessageSender`
配置 `无`
默认实现 `com.ratel.chat.netty.sender.SimpleMessageSender`



