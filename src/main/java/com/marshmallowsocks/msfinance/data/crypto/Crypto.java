package com.marshmallowsocks.msfinance.data.crypto;

import org.springframework.data.annotation.Id;

public class Crypto {

    @Id
    public String id;

    public String cryptoId;
    public String name;
    public String symbol;
    public Double holdings;
    public String userId;

    public Crypto(String cryptoId, String name, String symbol, Double holdings, String userId) {
        this.cryptoId = cryptoId;
        this.name = name;
        this.symbol = symbol;
        this.holdings = holdings;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "id='" + id + '\'' +
                ", cryptoId='" + cryptoId + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", holdings=" + holdings +
                ", userId='" + userId + '\'' +
                '}';
    }
}
