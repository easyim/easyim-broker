package com.broker.base.protocol.response;


/**
 * REST API 错误码
 *
 * @author hubin
 * @since 2017-06-26
 */
public enum RespApiErrorCode {
    /**
     * 失败
     */
    FAILED(-1, "操作失败"),
    /**
     * 成功
     */
    SUCCESS(0, "执行成功");

    private final long code;
    private final String msg;

    RespApiErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RespApiErrorCode fromCode(long code) {
        RespApiErrorCode[] ecs = RespApiErrorCode.values();
        for (RespApiErrorCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }


    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }
}
