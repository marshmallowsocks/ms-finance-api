package com.marshmallowsocks.msfinance.data.crypto;

import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.data.annotation.Id;

public class Crypto {

    @Id
    @GraphQLQuery(name = "id", description = "The id")
    private String id;

    @GraphQLQuery(name = "cryptoId", description = "The crypto's ID")
    private String cryptoId;
    @GraphQLQuery(name = "name", description = "The name of the currency")
    private String name;
    @GraphQLQuery(name = "symbol", description = "The crypto symbol")
    private String symbol;
    @GraphQLQuery(name = "holdings", description = "The user's holdings")
    private Double holdings;
    @GraphQLQuery(name = "userId", description = "The associated user's Id")
    private String userId;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getHoldings() {
        return holdings;
    }

    public void setHoldings(Double holdings) {
        this.holdings = holdings;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
