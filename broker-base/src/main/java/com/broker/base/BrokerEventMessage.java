package com.broker.base;

import com.broker.base.protocol.request.RequestMessage;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 11:23
 * Description: easyimbroker
 **/
public abstract class BrokerEventMessage {
    protected RequestMessage requestMessage;

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(RequestMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

}
