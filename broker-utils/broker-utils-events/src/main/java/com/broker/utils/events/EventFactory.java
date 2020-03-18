package com.broker.utils.events;

import com.broker.base.BrokerEventMessage;
import com.broker.base.IBrokerEventBus;
import com.broker.utils.events.rocketmq.RocketMQConfig;
import com.broker.utils.events.support.EventBusEvent;
import com.broker.utils.events.support.RocketMQEvent;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:31
 * Description: easyimbroker
 **/
public class EventFactory {
    public static final String EVENT_TYPE_EVENTBUS = "eventbus";
    public static final String EVENT_TYPE_ROCKETMQ = "rocketmq";

    private static IBrokerEventBus storage = null;
    // 懒加载
    public  static IBrokerEventBus getInstance(){
        if(storage == null){
            storage =  EventFactory.create();
            return storage;
        }
        else {
            return storage;
        }
    }
    public static  <T extends BrokerEventMessage> IBrokerEventBus create(){
        // 默认使用 google cache
        String eventUsing = "".equals(System.getProperty("broker.event.use", "")) ? EventFactory.EVENT_TYPE_EVENTBUS : System.getProperty("broker.event.use");
        if(EventFactory.EVENT_TYPE_EVENTBUS.equals(eventUsing)){
            return new EventBusEvent();
        }
        else if(EventFactory.EVENT_TYPE_ROCKETMQ.equals(eventUsing)){

            //设置redis配置项值
            RocketMQConfig rocketMQConfig = new RocketMQConfig();
            rocketMQConfig.setMqServerUrl(System.getProperty("broker.event.mq.server", "localhost:9876"));
            String point = System.getProperty("broker.event.mq.point", "");
            if("".equals(point)){
                throw new RuntimeException("Please special broker.event.mq.point value in system.property when using rocketmq.");
            }
            rocketMQConfig.setMqPoint(point);
            return new RocketMQEvent(rocketMQConfig);
        }
        else {
            throw new RuntimeException(" broker storage initialization failed, with setting: broker.storage.use=" + System.getProperty("broker.storage.use"));
        }
    }
}
