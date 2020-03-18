package com.broker.base.event;

import com.broker.base.BrokerEventMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class ClientSendEvent extends BrokerEventMessage {
    private String appKey;
    private String clientId;// socket客户端id
    private String mid; // 消息uid
    private String from; // from user auid
    private String to; // to user auid
    private String way; // P2P, P2R
    private String msgBody;
}
