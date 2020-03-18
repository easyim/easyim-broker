package com.broker.hook.support;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.auth.TokenProvider;
import com.broker.base.auth.UserJwt;
import com.broker.base.event.ClientRoomMemberEvent;
import com.broker.base.protocol.hook.UserLoginForm;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.protocol.response.Resp;
import com.broker.base.protocol.response.ResponseMessage;
import com.broker.base.storage.BrokerStorageEntry;
import com.broker.base.utils.ObjectUtils;
import com.broker.hook.BrokerLinkableHook;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;

/**
 *  socketio 鉴权组件。1. 用户登录; 2. 记录用户的信息
 *
 * */
@Slf4j
public class ClientAuthCollectorHook extends BrokerLinkableHook {
    private final String machineId;
    private IStorage storage = null;
    private IBrokerEventBus event = null;

    public ClientAuthCollectorHook(String machineId) {
        this.machineId = machineId;
    }

    @Override
    public void startup(SocketIOServer socket, IStorage storage, IBrokerEventBus eventBus) {
        this.storage = storage;
        this.event = eventBus;
    }

    public void onConnected(SocketIOClient client) {
    }

    public void onDisConnected(SocketIOClient client) {

    }

    @Override
    public void onReceiveMessage(SocketIOClient client, RequestMessage message, AckRequest ackSender) {
        if("topic.connection".equals(message.getTopic()) && "authority/request".equals(message.getMethod())){
            UserLoginForm loginForm = ObjectUtils.copy(message.getBody(), UserLoginForm.class);
            Resp<String> resp = doAuthUser(loginForm);
            ResponseMessage<String> responseMessage = new ResponseMessage<String>()
                    .setRequestId(message.getRequestId())
                    .setResponse(resp);
            if(resp.ok()){
                // 用户认证收集器
                this.storage.setKeyValue(BrokerStorageEntry.CLIENT_TO_LOGINUSER, client.getSessionId().toString(), loginForm.getAuid());
                // 更新群信息
                this.event.send(new ClientRoomMemberEvent().setClientId(client.getSessionId().toString()));
            }
            ackSender.sendAckData(responseMessage);
        }
        else {
          if(next != null){
            next.onReceiveMessage(client, message, ackSender);
          }
        }
    }

    private Resp<String> doAuthUser(UserLoginForm loginForm){
        log.debug(" authority: " + ObjectUtils.json(loginForm));
        String value = "";
        try{
            value = this.storage.getValue(BrokerStorageEntry.CLIENT_USER_TOKEN, loginForm.getToken());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(ObjectUtils.strEmpty(value) || !value.equals(loginForm.getToken())){
            log.info(" auid or token incorrect.");
            return Resp.failed(" auid or token incorrect.");
        }

        TokenProvider token = new TokenProvider();
        UserJwt userJwt = new UserJwt();
        userJwt.setAuid(loginForm.getAuid());
        userJwt.setAppKey(loginForm.getAppKey());
        String jwt = token.createToken(userJwt);

        return Resp.ok(jwt);
    }
}
