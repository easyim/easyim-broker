package com.broker.base.protocol.hook;


import com.broker.base.protocol.ProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UserLoginForm extends ProtocolMessage {
    private String appKey = ""; // appKey
    private String auid = ""; // 用户登录ID
    private String token = ""; // 用户登录token, 可以理解为密码
}
