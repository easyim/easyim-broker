package com.broker.base;

public interface IBrokerEventBus {
     void send(BrokerEventMessage message);
     // 向对应的主机发消息
     void send(String host, BrokerEventMessage message);

     void register(Object handler);

     void unregister(Object handler);

     void destroy();
}
