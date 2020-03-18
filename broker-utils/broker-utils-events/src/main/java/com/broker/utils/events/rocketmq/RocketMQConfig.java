package com.broker.utils.events.rocketmq;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/18 9:54
 * Description: easyimbroker
 **/

@Accessors(chain = true)
@Data
public class RocketMQConfig {
    private String mqServerUrl = "localhost:9876"; // mq服务器地址, 消费者和生产者使用同一个地址, 多个地址用;分割
    private String mqPoint = ""; // 连接到mq服务器的节点名字, 可以是本机,也可能是其它mq连接者
}
