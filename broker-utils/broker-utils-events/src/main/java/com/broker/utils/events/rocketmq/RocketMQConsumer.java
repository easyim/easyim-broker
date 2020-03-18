package com.broker.utils.events.rocketmq;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.*;
import java.util.function.BiConsumer;

@Slf4j
public class RocketMQConsumer {
    private final RocketMQConfig rocketMQConfig;
    private DefaultMQPushConsumer consumer = null;
    Map<TopicTag, List<BiConsumer<String, String>>> topicConsumers = new HashMap<>();
    public RocketMQConsumer(RocketMQConfig rocketMQConfig){
        this.rocketMQConfig = rocketMQConfig;
        init();
    }

    private void init(){
        //设置消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_LRW_DEV_SUBS");
        consumer.setVipChannelEnabled(false);
        consumer.setNamesrvAddr(rocketMQConfig.getMqServerUrl());
        //设置消费者端消息拉取策略，表示从哪里开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        this.consumer = consumer;
        //消费者端启动消息监听，一旦生产者发送消息被监听到，就打印消息，和rabbitmq中的handlerDelivery类似
        this.consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : msgs) {
                    String topic = messageExt.getTopic();
                    String tag = messageExt.getTags();
                    String msg = new String(messageExt.getBody());
                    List<BiConsumer<String, String>> consumers = topicConsumers.getOrDefault(new TopicTag().setTopic(topic).setTag(tag), new ArrayList<>());
                    if(!consumers.isEmpty()){
                        log.debug("RocketMQ: 消费消息ID:" + messageExt.getMsgId() +"   TOPIC:" + topic +"   OBJECT:" + tag);
                        consumers.forEach(c->{
                            c.accept(tag, msg);
                        });
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }
    // 添加订阅
    public void appendSubscribe(String topic, String tag, BiConsumer<String, String> consumer){
        List<BiConsumer<String, String>> consumers = topicConsumers.getOrDefault(new TopicTag().setTopic(topic).setTag(tag), new ArrayList<>());
        consumers.add(consumer);
        topicConsumers.put(new TopicTag().setTopic(topic).setTag(tag), consumers);
    }

    public void start(String topic){
        try {
            this.consumer.subscribe(topic, "*");
            this.consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.consumer.shutdown();
    }

    @Accessors(chain = true)
    @Data
    private static class TopicTag{
        private String topic;
        private String tag;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TopicTag topicTag = (TopicTag) o;
            return Objects.equals(topic, topicTag.topic) &&
                    Objects.equals(tag, topicTag.tag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(topic, tag);
        }
    }
}
