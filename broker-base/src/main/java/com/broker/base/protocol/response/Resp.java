package com.broker.base.protocol.response;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 14:11
 * Description: easyimbroker
 **/

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * REST API 返回结果
 *
 * @author hubin
 * @since 2018-06-05
 */
@Data
@Accessors(chain = true)
public class Resp<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码
     */
    private long code;
    /**
     * 结果集
     */
    private T data;
    /**
     * 描述
     */
    private String msg;

    public Resp() {
    }

    public static <T> Resp<T> ok(T data) {
        return restResult(data, 0, "success");
    }

    public static <T> Resp<T> failed(T msg) {
        return restResult(null, RespApiErrorCode.FAILED.getCode(), String.valueOf(msg));
    }

    public static  Resp failedMsg(String msg) {
        return restResult(null, RespApiErrorCode.FAILED.getCode(), msg);
    }


    private static <T> Resp<T> restResult(T data, long code, String msg) {
        Resp<T> apiResult = new Resp<T>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public boolean ok() {
        return RespApiErrorCode.SUCCESS.getCode() == code;
    }


}

