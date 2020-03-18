package com.broker.hook.support;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.storage.BrokerStorageEntry;
import com.broker.hook.BrokerLinkableHook;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 *  socketio 连接收集器，收集连接到本机socket客户端的连接信息, 保存到broker storage
 *
 * */
public class ClientConnectionCollectorHook extends BrokerLinkableHook {
    private IStorage storage = null;
    private String machineId;
    private static final Object lock = new Object();

    public ClientConnectionCollectorHook(String machineId){
        this.machineId = machineId;
    }

    @Override
    public void startup(SocketIOServer socketIOServer, IStorage storage, IBrokerEventBus iBrokerEventBus) {
        this.storage = storage;
    }

    @Override
    public void onConnected(SocketIOClient socketIOClient) {
        String currentClientId = socketIOClient.getSessionId().toString();
        TreeSet<String> clientsNotRepeat = getClients();
        clientsNotRepeat.add(currentClientId);// 添加新客户端
        synchronized (lock){
            this.storage.setKeyValue(BrokerStorageEntry.MACHINE_CLIENTS, machineId, String.join(",", clientsNotRepeat));
        }
    }

    @Override
    public void onDisConnected(SocketIOClient socketIOClient) {
        String currentClientId = socketIOClient.getSessionId().toString();
        Set<String> clientsNotRepeat = getClients();
        clientsNotRepeat.remove(currentClientId);// 移除客户端
        synchronized (lock){
            this.storage.setKeyValue(BrokerStorageEntry.MACHINE_CLIENTS, machineId, String.join(",", clientsNotRepeat));
        }

        this.storage.remove(BrokerStorageEntry.CLIENT_TO_LOGINUSER, currentClientId);
    }

    private TreeSet<String> getClients(){
        String clientStr = this.storage.getValue(BrokerStorageEntry.MACHINE_CLIENTS, machineId);
        String[] clients = clientStr.split(",");
        return new TreeSet<>(Arrays.asList(clients));
    }
}
