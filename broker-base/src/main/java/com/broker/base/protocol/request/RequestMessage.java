package com.broker.base.protocol.request;

import com.broker.base.protocol.ProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class RequestMessage<T extends ProtocolMessage> {
    private String topic = "";
    private String requestId = "";
    private String method = "";
    private T protocolMessage = null;
    private Object body = null;
}
