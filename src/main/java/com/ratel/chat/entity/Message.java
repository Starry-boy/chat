package com.ratel.chat.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 信息实体
 * @Author ratel
 * @Date 2020/1/14
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class Message implements Serializable {
    /** 消息发送者 */
    private Long from;
    /** 消息接收者 */
    private Long to;
    /** 版本 */
    private String version;
    /** 群 */
    private Long group;
    /** 消息类型 */
    private String type;
    /** 消息内容 */
    private String content;
    /** 加密方式 */
    private String encryption;
    /** 设备 */
    private String device;
    /** 发送时间 */
    private Date sendDate;
    /** 扩展字段 :格式 [{"key":value}] */
    private String extras;
}
