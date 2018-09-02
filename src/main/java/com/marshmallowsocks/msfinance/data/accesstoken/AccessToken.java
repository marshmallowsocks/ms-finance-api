package com.marshmallowsocks.msfinance.data.accesstoken;

import org.springframework.data.annotation.Id;

public class AccessToken {

    @Id
    public String id;

    public String accessToken;
    public String itemToken;
    public String userId;

    public AccessToken(String accessToken, String itemToken, String userId) {
        this.accessToken = accessToken;
        this.itemToken = itemToken;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", itemToken='" + itemToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
