package com.broker.distribute;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.protocol.ProtocolMessage;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.utils.ObjectUtils;
import com.broker.hook.BrokerLinkableHook;
import com.broker.utils.events.EventFactory;
import com.broker.utils.strorage.StorageFactory;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 13:52
 * Description: easyimbroker
 **/
public class JavaMainDistribute {

    /**
     socketio:
     host: localhost
     port: 9091
     maxFramePayloadLength: 1048576
     maxHttpContentLength: 1048576
     bossCount: 1
     workCount: 100
     allowCustomRequests: true
     upgradeTimeout: 1000000
     pingTimeout: 6000000
     pingInterval: 25000
     * */
    public SocketIOServer initSocketIOServer(String host, Integer port) {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(1);
        config.setWorkerThreads(100);
        config.setAllowCustomRequests(true);
        config.setUpgradeTimeout(1000000);
        config.setPingTimeout(6000000);
        config.setPingInterval(25000);
        return new SocketIOServer(config);
    }


    // 配置中间件
    public void configBroker(SocketIOServer socketIOServer, LinkedList<BrokerLinkableHook> hooks){
        IStorage storage = createBrokerStorage();
        IBrokerEventBus eventBus = createBrokerEventBus();
//        LinkedList<BrokerLinkableHook> hooks = createBrokerHooks();
        // 插件初始化
        hooks.forEach(h->{
            h.startup(socketIOServer, storage, eventBus);
        });

        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            hooks.forEach(e->{
                e.onConnected(client);
            });
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            hooks.forEach(e->{
                e.onDisConnected(client);
            });
        });


        // 所有的topic
        List<String> topics = Arrays.asList(
                "topic.error",
                "topic.connection",
                "topic.user"
        );

        // 处理自定义的事件，与连接监听类似
        topics.forEach(e->{
            socketIOServer.addEventListener(e, Object.class, (client, msgBody, ackSender) -> {
//                ProtocolMessage message = Beans.beans(msgBody.toString(), ProtocolMessage.class);
                ProtocolMessage message = ObjectUtils.copy(msgBody, ProtocolMessage.class);
                if(message != null){
                    if(hooks.size() > 0){
                        // 使用责任链模式, 将onReceiveMessage 依次向后传递
                        hooks.get(0).onReceiveMessage(
                                client,
                                new RequestMessage<ProtocolMessage>()
                                        .setTopic(e)
                                        .setMethod(message.getMethod())
                                        .setRequestId(message.getRequestId())
                                        .setBody(msgBody)
                                        .setProtocolMessage(message)
                                ,ackSender
                        );
                    }
                }
            });
        });
        socketIOServer.start();
    }


    // 共享存储
    private IStorage createBrokerStorage(){
        return StorageFactory.getInstance();
    }

    // 事件通知
    private IBrokerEventBus createBrokerEventBus(){
        return EventFactory.getInstance();
    }

    // 将插件一个接一个连接起来
    public LinkedList<BrokerLinkableHook> createBrokerLinkableHooks(LinkedList<BrokerLinkableHook> hooks){
        for(int i = 0; i < hooks.size(); i++){
            if( i == hooks.size()-1 ){
                hooks.get(i).setNext(null); // 没有后续插件
            }
            else {
                hooks.get(i).setNext(hooks.get(i+1));
            }
        }
        return hooks;
    }


}
