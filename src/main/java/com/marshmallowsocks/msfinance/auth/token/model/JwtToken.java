package com.marshmallowsocks.msfinance.auth.token.model;

public class JwtToken {
    private String token;
    private long expirationMillis;

    public JwtToken(String token, Long expirationMillis) {
        this.token = token;
        this.expirationMillis = expirationMillis;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpirationMillis() {
        return expirationMillis;
    }

    public void setExpirationMillis(long expirationSec) {
        this.expirationMillis = expirationSec;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationMillis && expirationMillis != -1;
    }

    public boolean isPermanent() {
        return expirationMillis == -1;
    }
}
