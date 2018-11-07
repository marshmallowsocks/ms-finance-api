package com.marshmallowsocks.msfinance.data.accesstoken;

import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.data.annotation.Id;

public class AccessToken {

    @Id
    @GraphQLQuery(name = "id", description="The plaid access token ID")
    private String id;

    @GraphQLQuery(name = "accessToken", description = "The plaid access token")
    private String accessToken;
    @GraphQLQuery(name = "itemToken", description = "The associated item's token")
    private String itemToken;
    @GraphQLQuery(name = "userId", description = "The associated user's ID")
    private String userId;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getItemToken() {
        return itemToken;
    }

    public void setItemToken(String itemToken) {
        this.itemToken = itemToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
