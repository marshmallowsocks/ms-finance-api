package com.marshmallowsocks.msfinance.data.groups;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Group {

    @Id
    public String id;

    public String name;
    public List<String> accounts;
    public String userId;

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
}
