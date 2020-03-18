package com.broker.utils.events.rocketmq;

import com.broker.base.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class RocketMQProducer {
    private final RocketMQConfig rocketMQConfig;
    private DefaultMQProducer producer = null;
    Map<String, List<Consumer<Object>>> topicConsumers = new HashMap<>();
    public RocketMQProducer(RocketMQConfig rocketMQConfig){
        this.rocketMQConfig = rocketMQConfig;
        init();
    }

    private void init(){
        //需要一个producer group名字作为构造方法的参数，这里为producer1
        DefaultMQProducer producer = new DefaultMQProducer("producer1");
        //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr(rocketMQConfig.getMqServerUrl());
        producer.setVipChannelEnabled(false);

        //为避免程序启动的时候报错，添加此代码，可以让rocketMq自动创建topickey
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");

        this.producer = producer;
    }
    // 添加订阅
    public void sendMessage(String topic, Object msg) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message(topic, msg.getClass().getName(),
                (ObjectUtils.json(msg)).getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = this.producer.send(message);
        log.debug("RocketMQ: 发送的消息ID:" + sendResult.getMsgId()  +"    TOPIC:" + topic +"   OBJECT:" + msg.getClass().getName() +"    消息的状态:" + sendResult.getSendStatus());
    }

    public void start(){
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.producer.shutdown();
    }
}
