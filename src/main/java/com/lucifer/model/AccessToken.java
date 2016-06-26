package com.lucifer.model;

/**
 * Created by Administrator on 2016/6/26.
 */
public class AccessToken {

    private Long userId;

    private String accessToken;

    private String code;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
