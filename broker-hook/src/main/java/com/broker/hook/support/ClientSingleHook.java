package com.broker.hook.support;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.event.ClientSendEvent;
import com.broker.base.event.ClusterDispatcherEvent;
import com.broker.base.event.MQSubscribe;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.storage.BrokerStorageEntry;
import com.broker.base.utils.ObjectUtils;
import com.broker.hook.BrokerLinkableHook;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  socketio 单机路由组件, 处理请求转发的的消息
 *
 * */
@Slf4j
public class ClientSingleHook extends BrokerLinkableHook {
    private IStorage storage = null;
    private IBrokerEventBus event = null;
    private String machineId = "";
    private SocketIOServer socket;
    public ClientSingleHook(String machineId) {
        this.machineId = machineId;
    }

    @Override
    public void startup(SocketIOServer socket, IStorage storage, IBrokerEventBus eventBus) {
        this.socket = socket;
        this.storage = storage;
        this.event = eventBus;
        this.event.register(this);
    }

    public void onReceiveMessage(SocketIOClient client, RequestMessage message, AckRequest ackSender) {
        if (this.next != null) {
            this.next.onReceiveMessage(client, message, ackSender);
        }
    }


    public void onConnected(SocketIOClient client) {

    }

    public void onDisConnected(SocketIOClient client) {

    }


    /**
     *  功能: 路由普通消息/群消息
     * */
    @MQSubscribe
    @Subscribe
    public void onReceiveClusterDispatcherMessage(ClusterDispatcherEvent message) {
        log.info("msg[{}]:{} was dispatcher to MACHINE:{}", message.getMid(), message.getMsgBody(), machineId);
        // 客户端已连接到当前机器上, 发eventBus消息
        if(message.getOffline() == 1 || message.getWay().equals("P2P")){
            SocketIOClient client = findClientWithTargetAuid(message.getTo());
            if(client == null){
                return;
            }
            this.event.send(
                    ObjectUtils.copy(message, ClientSendEvent.class)
                            .setClientId(client.getSessionId().toString())
            );
        }
        else if(message.getWay().equals("P2R")){
            // 人对群: 发多份消息
            Map<String, String> userToClientMap = getCacheUserToClientMap(); // 获取已连接客户端的用户列表
            userToClientMap.entrySet()
                    .stream()
                    .filter(v -> message.getRoomMembers().contains(v.getKey()))
                    .forEach((e)->{
                        SocketIOClient roomMemberClient = findClientWithId(e.getValue());
                        if(roomMemberClient != null){
                            this.event.send(
                                    ObjectUtils.copy(message, ClientSendEvent.class)
                                            .setClientId(roomMemberClient.getSessionId().toString())
                            );
                        }
            });
        }
    }

    @Nullable
    private SocketIOClient findClientWithTargetAuid(String toUserAuid){
        String clientId = findCacheClientIdWithUserAuid(toUserAuid);
        if("".equals(clientId)){
            log.info(String.format("client:%s offline", clientId));
            return null;
        }
        SocketIOClient client = findClientWithId(clientId);
        if(client == null){
            log.info(String.format("client:%s offline", clientId));
            return null;
        }
        return client;
    }

    private Map<String, String> getCacheUserToClientMap(){
        List<String> clientIds = socket.getAllClients().stream().map(v->v.getSessionId().toString()).collect(Collectors.toList());
        Map<String, String> userToClientMap = new HashMap<>();
        this.storage.getAllKeyValues(BrokerStorageEntry.CLIENT_TO_LOGINUSER).forEach(((k, v)->{
            if(clientIds.contains(k)){
                userToClientMap.put(v, k);
            }
        }));
        return userToClientMap;
    }

    private String findCacheClientIdWithUserAuid(String toUserAuid){
        Map<String, String> userToClientMap = getCacheUserToClientMap();
        return userToClientMap.getOrDefault(toUserAuid, "");
    }

    @Nullable
    private SocketIOClient findClientWithId(String clientId){
        return socket.getAllClients().stream().filter(v->v.getSessionId().toString().equals(clientId)).findFirst().orElse(null);
    }
}
