package com.broker.base.event;

import com.broker.base.BrokerEventMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class ClientRoomMemberEvent extends BrokerEventMessage {
    private String clientId;
}
