package com.marshmallowsocks.msfinance.data.user;

import org.springframework.data.annotation.Id;

public class UserModel {

    @Id
    public String id;

    public String userName;
    public String password;

    public UserModel() {}

    public UserModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "UserModel[id=%s, userName=%s]",
                id, userName
        );
    }
}
