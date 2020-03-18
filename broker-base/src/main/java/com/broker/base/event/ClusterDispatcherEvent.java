package com.broker.base.event;

import com.broker.base.BrokerEventMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class ClusterDispatcherEvent extends BrokerEventMessage {
    private String appKey = ""; // 平台sdk
    private String mid = ""; // 消息uid
    private String from = ""; // from user auid
    private String to = ""; // to user auid(way = P2P) ; to room uid(way = P2R)
    private List<String> roomMembers = new ArrayList<>(); // 若way = P2R, 这是群所有成员的auid
    private String way = ""; // P2P,P2R,P2HR
    private String msgBody = "";
    private Integer offline = 0; // 0: 普通消息; 1: 离线消息
}
