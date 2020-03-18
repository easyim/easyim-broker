package com.broker.base;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 10:09
 * Description: easyimbroker
 **/
public class StorageException extends RuntimeException {
    private String msg = "";
    public StorageException(String message){
        super(message);
        this.msg = message;
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
