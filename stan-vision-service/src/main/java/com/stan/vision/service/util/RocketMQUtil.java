package com.stan.vision.service.util;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

public class RocketMQUtil {

    // Synchronously send msg
    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws Exception{
        SendResult result = producer.send(msg);
        System.out.println(result);
    }

    // asynchronously send msg: when do not care about feedback of sending msg
    public static void asyncSendMsg(DefaultMQProducer producer, Message msg) throws Exception{
        int messageCount = 2;
        CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for(int i = 0; i < messageCount; ++i){
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println(sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.println("Exception occurred when sending message." + e);
                    e.printStackTrace();
                }
            });
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        return;
    }

}
