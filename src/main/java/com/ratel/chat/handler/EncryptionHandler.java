package com.ratel.chat.handler;

import com.ratel.chat.entity.Message;

/**
 * @Description 加密处理器
 * @Author ratel
 * @Date 2020/1/15
 * @Version 1.0
 */
public interface EncryptionHandler {

    /**
     * @return
     * @Description 加密
     * @Date        下午3:58 2020/1/15
     * @Author      ratel
     * @Param
     **/
    void encryption(Message message);

    /**
     * @return
     * @Description 解密
     * @Date        下午3:58 2020/1/15
     * @Author      ratel
     * @Param
     **/
    void decrypt(Message message);
}
