package com.marshmallowsocks.msfinance.core.group;

import java.util.List;

public class CreateGroupRequest {
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

    public String name;
    public List<String> accounts;
}
