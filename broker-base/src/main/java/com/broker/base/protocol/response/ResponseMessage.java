package com.broker.base.protocol.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class ResponseMessage<T>{
    private String requestId = "";// 单次请求requestId
    private Resp<T> response = Resp.ok(null);

    public ResponseMessage(String requestId, boolean succeed, T failMsg){
        this.requestId = requestId;
        if(succeed){
            this.response = Resp.ok(null);
        }
        else {
            this.response = Resp.failed(failMsg);
        }
    }

    public ResponseMessage(boolean succeed, T failMsg){
        if(succeed){
            this.response = Resp.ok(null);
        }
        else {
            this.response = Resp.failed(failMsg);
        }
    }

    public ResponseMessage(boolean succeed){
        if(succeed){
            this.response = Resp.ok(null);
        }
        else {
            this.response = (Resp<T>) Resp.failed("");
        }
    }


    public static ResponseMessage succeed(String requestId){
        return new ResponseMessage<String>()
                .setRequestId(requestId)
                .setResponse(Resp.ok("ok"));
    }

    public static ResponseMessage failed(String requestId, String msg){
        return new ResponseMessage<String>().setResponse(Resp.failed(msg));
    }
}
