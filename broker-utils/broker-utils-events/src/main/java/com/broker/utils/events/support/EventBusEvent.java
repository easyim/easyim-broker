package com.broker.utils.events.support;


import com.broker.base.BrokerEventMessage;
import com.broker.base.IBrokerEventBus;



/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:29
 * Description: easyimbroker
 **/
public class EventBusEvent implements IBrokerEventBus {
    /** 事件任务总线 */
    private static com.google.common.eventbus.EventBus eventBus = null;

    public EventBusEvent(){
        eventBus = new com.google.common.eventbus.EventBus();
    }

    public void send(BrokerEventMessage message) {
        eventBus.post(message);
    }

    @Override
    public void send(String host, BrokerEventMessage message) {
        eventBus.post(message);
    }

    /**
     * 注册事件处理器
     *
     * @param handler
     */
    public void register(Object handler) {
        eventBus.register(handler);
    }
    /**
     * 注销事件处理器
     *
     * @param handler
     */
    public void unregister(Object handler) {
        eventBus.unregister(handler);
    }

    public void destroy(){

    }
}
