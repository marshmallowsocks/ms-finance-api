package com.marshmallowsocks.msfinance.data.authtoken;

import org.springframework.data.annotation.Id;

public class InvalidToken {

    @Id
    public String id;

    public String token;

    public InvalidToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "InvalidToken{" +
                "id='" + id + '\'' +
                ", authtoken='" + token + '\'' +
                '}';
    }
}
