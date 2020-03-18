package com.broker.base;


import com.broker.base.protocol.request.RequestMessage;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:31
 * Description: easyimbroker  IBrokerEventBus<K extends BrokerEventMessage>
 **/
public interface IBrokerHook {
     void startup(SocketIOServer socket, IStorage storage, IBrokerEventBus eventBus); // 插件初始化
     void onConnected(SocketIOClient client);
     void onDisConnected(SocketIOClient client);
     void onReceiveMessage(SocketIOClient client, RequestMessage message, AckRequest ackSender);
}
