package com.broker.base.auth;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/11/28 11:42
 * Description: education
 **/

@Accessors(chain = true)
@Data
public class UserJwt {
    private String auid = ""; // 用户auid(唯一性)
    private String appKey = ""; // appKey(唯一性)

    public static final String USER_AUID = "USER_AUID";
    public static final String APP_KEY = "APP_KEY";
    public boolean isEmpty(){
        return this.auid == null || "".equals(this.auid);
    }
}
