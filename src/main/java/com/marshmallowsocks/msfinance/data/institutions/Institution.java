package com.marshmallowsocks.msfinance.data.institutions;

import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.data.annotation.Id;

public class Institution {

    @Id
    private String id;

    @GraphQLQuery(name = "name", description = "The institution name")
    private String name;
    @GraphQLQuery(name = "institutionId", description = "The institution's ID")
    private String institutionId;
    @GraphQLQuery(name = "itemToken", description = "The item token")
    private String itemToken;
    @GraphQLQuery(name = "products", description = "The available products of this institution")
    private String products;
    @GraphQLQuery(name = "userId", description = "The associated user ID")
    private String userId;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getItemToken() {
        return itemToken;
    }

    public void setItemToken(String itemToken) {
        this.itemToken = itemToken;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
