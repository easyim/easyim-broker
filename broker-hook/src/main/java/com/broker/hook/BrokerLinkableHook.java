package com.broker.hook;


import com.broker.base.IBrokerEventBus;
import com.broker.base.IBrokerHook;
import com.broker.base.IStorage;
import com.broker.base.protocol.request.RequestMessage;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;


/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 13:30
 * Description: easyimbroker
 **/
public abstract class BrokerLinkableHook  implements IBrokerHook  {
    protected BrokerLinkableHook next = null;
    public void setNext(BrokerLinkableHook next) {
        this.next = next;
    }
    public abstract void startup(SocketIOServer socket, IStorage storage, IBrokerEventBus eventBus);
    public abstract void onConnected(SocketIOClient client);
    public abstract void onDisConnected(SocketIOClient client);
    public void onReceiveMessage(SocketIOClient client, RequestMessage message, AckRequest ackSender){
        if(next != null){
            next.onReceiveMessage(client, message, ackSender);
        }
    }
}
