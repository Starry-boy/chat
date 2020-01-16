package com.ratel.chat.netty.manager;

import com.ratel.chat.entity.Message;
import com.ratel.chat.handler.MessageForwardHandler;
import com.ratel.chat.properties.ChatProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

/**
 * @Description
 * @Author ratel
 * @Date 2020/1/16
 * @Version 1.0
 */
@Log4j2
public class MessageManger {
    @Autowired
    private MessageForwardHandler messageForwardHandler;
    @Data
    @Accessors(chain = true)
    class PersistenceMessage implements Runnable{
        private MessageForwardHandler messageForwardHandler;
        private Message message;
        @Override
        public void run() {
            messageForwardHandler.redirectMessage(message);
        }
    }
    /**
     * 存储消息的阻塞队列
     */
    private static  BlockingQueue<Message> blockingQueue;

    private static  ThreadPoolExecutor threadPool;


    /**
     * @return
     * @Description 队列满了后 返回 false
     * @Date 上午11:35 2020/1/16
     * @Author ratel
     * @Param
     **/
    public static boolean offer(Message message) {
        return blockingQueue.offer(message);
    }

    /**
     * @return
     * @Description 拉取消息
     * @Date 上午11:38 2020/1/16
     * @Author ratel
     * @Param
     **/
    public static Message take() throws InterruptedException {
        return blockingQueue.take();
    }

    public void init(ThreadPoolExecutor threadPool,BlockingQueue<Message> blockingQueue){
        this.threadPool = threadPool;
        this.blockingQueue = blockingQueue;
    }

    public void consumeMessage(){
        new Thread(() -> {
            try {
                while (true){
                    Message message = this.take();
                    threadPool.execute(new PersistenceMessage()
                            .setMessageForwardHandler(messageForwardHandler)
                            .setMessage(message)
                    );
                }
            } catch (Exception e) {
                log.error(">>> 从阻塞队列中 获取信息失败！：{}",e);
            }
        }).start();
    }
}
