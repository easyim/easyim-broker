package com.broker.utils.events.support;

import com.broker.base.BrokerEventMessage;
import com.broker.base.IBrokerEventBus;
import com.broker.base.event.MQSubscribe;
import com.broker.base.utils.ObjectUtils;
import com.broker.utils.events.rocketmq.RocketMQConfig;
import com.broker.utils.events.rocketmq.RocketMQConsumer;
import com.broker.utils.events.rocketmq.RocketMQProducer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:29
 * Description: easyimbroker
 **/
public class RocketMQEvent implements IBrokerEventBus {
    private RocketMQConsumer mqConsumer;
    private RocketMQProducer mqProducer;
    private RocketMQConfig rocketMQConfig;
    private String subScribeTopic = "";

    public RocketMQEvent(RocketMQConfig rocketMQConfig){
        String point = rocketMQConfig.getMqPoint();
        this.subScribeTopic = "IM-BROKER-" + point;
        this.rocketMQConfig = rocketMQConfig;

        mqConsumer = new RocketMQConsumer(rocketMQConfig);
        mqConsumer.start(subScribeTopic);

        mqProducer = new RocketMQProducer(rocketMQConfig);
        mqProducer.start();
    }


    public void send(BrokerEventMessage message) {
        this.send(this.rocketMQConfig.getMqPoint(), message);
    }

    public void send(String point, BrokerEventMessage message){
        String sendTopic = "IM-BROKER-" + point;
        try {
            mqProducer.sendMessage(sendTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册事件处理器
     *
     * @param handler
     */
    public void register(Object handler) {

        Arrays.asList(handler.getClass().getMethods()).forEach(method -> {
            MQSubscribe subscribe = method.getAnnotation(MQSubscribe.class);
            if(subscribe != null){
                // 把MQ的消费者绑定到 handler 对象标注了 MQSubscribe 注解的方法上.
                InvokeInfo invokeInfo = new InvokeInfo();
                Class<?>[] methodParameterTypes = method.getParameterTypes();
                if(methodParameterTypes.length > 0){
                    invokeInfo.setBean(handler)
                            .setMethod(method)
                            .setMethodFirstParamType(methodParameterTypes[0]);
                }

                if(!invokeInfo.isEmpty()){
                    mqConsumer.appendSubscribe(this.subScribeTopic, invokeInfo.methodFirstParamType.getName(), (tag, msg)->{
                        if(tag.equals(invokeInfo.methodFirstParamType.getName())){
                            try {
                                Object arg = ObjectUtils.beans(msg, invokeInfo.methodFirstParamType);
                                invokeInfo.getMethod().invoke(invokeInfo.bean, arg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


    }
    /**
     * 注销事件处理器
     *
     * @param handler
     */
    public void unregister(Object handler) {

    }

    @Accessors(chain = true)
    @Data
    private static class InvokeInfo{
        private Object bean;
        private Method method;
        private Class<?>  methodFirstParamType;

        public boolean isEmpty(){
            return bean == null || method == null || methodFirstParamType == null;
        }
    }

    public void destroy(){
        if(mqConsumer != null){
            mqConsumer.stop();
        }
        if(mqProducer != null){
            mqProducer.stop();
        }
    }
}
