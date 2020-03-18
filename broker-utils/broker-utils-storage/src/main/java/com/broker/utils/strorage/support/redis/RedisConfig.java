package com.broker.utils.strorage.support.redis;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/18 9:35
 * Description: easyimbroker
 **/

@Accessors(chain = true)
@Data
public class RedisConfig {
    private String host = "localhost";
    private Integer port = 6379;
    private Integer maxActive = 0;
    private Integer maxIdle = 0;
    private Integer maxWait = 0;
}
