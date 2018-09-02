package com.marshmallowsocks.msfinance.data.institutions;

import org.springframework.data.annotation.Id;

public class Institution {

    @Id
    public String id;

    public String name;
    public String institutionId;
    public String itemToken;
    public String products;
    public String userId;

    public Institution(String name, String institutionId, String itemToken, String products, String userId) {
        this.name = name;
        this.institutionId = institutionId;
        this.itemToken = itemToken;
        this.products = products;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", institutionId='" + institutionId + '\'' +
                ", itemToken='" + itemToken + '\'' +
                ", products='" + products + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
