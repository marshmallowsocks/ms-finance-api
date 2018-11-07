package com.marshmallowsocks.msfinance.data.groups;

import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Group {

    @Id
    @GraphQLQuery(name = "id", description = "The id")
    private String id;

    @GraphQLQuery(name = "name", description = "The group name")
    private String name;
    @GraphQLQuery(name = "accounts", description = "A list of account ID's")
    private List<String> accounts;
    @GraphQLQuery(name = "userId", description = "The user's ID")
    private String userId;

    public Group(String name, List<String> accounts, String userId) {
        this.name = name;
        this.accounts = accounts;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", accounts=" + accounts +
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

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
