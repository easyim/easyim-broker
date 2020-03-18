package com.broker.base.protocol;


import com.broker.base.auth.TokenProvider;
import com.broker.base.auth.UserJwt;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class ProtocolMessage {
    private String clientId = "";// 长连接sessionId
    private String requestId = "";// 单次请求requestId
    private Long timestamp = 0L; // 消息时间撮
    private String method = "";// 消息处理方法
    private String jwt  = "";// token

    public UserJwt getUserJwt(){
        if("".equals(this.jwt)){
            return new UserJwt();
        }
        return new TokenProvider().getUserJwt(this.jwt);
    }

    public ProtocolMessage(String clientId){
        this.clientId = clientId;
    }

}
