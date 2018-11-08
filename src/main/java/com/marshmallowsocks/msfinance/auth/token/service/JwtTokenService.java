package com.marshmallowsocks.msfinance.auth.token.service;

import com.marshmallowsocks.msfinance.auth.token.manager.JwtTokenManager;
import com.marshmallowsocks.msfinance.auth.token.model.JwtToken;
import com.marshmallowsocks.msfinance.util.DateService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@Service
public class JwtTokenService implements Clock, TokenService {

    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private DateService dateService;
    private String issuer;
    private int expirationSec;
    private int clockSkewSec;
    private String secretKey;

    private JwtTokenManager tokenManager;

    public JwtTokenService(
            @Autowired final DateService dateService,
            @Autowired final JwtTokenManager jwtTokenManager,
            @Value("${jwt.issuer:ms-finance}") String issuer,
            @Value("${jwt.expiration-sec:1800}") int expirationSec,
            @Value("${jwt.clock-skew-sec:300}") int clockSkewSec,
            @Value("${jwt.secret}") String secretKey) {
        this.dateService = dateService;
        this.issuer = issuer;
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;
        this.secretKey = secretKey;
        this.tokenManager = jwtTokenManager;
    }

    @Override
    public JwtToken permanent(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public JwtToken expiring(Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    @Override
    public Map<String, String> untrusted(String token) {
        JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec);

        String withoutSignature = substringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
    }

    @Override
    public Map<String, String> verify(String token) {
        if(tokenManager.isInvalid(token)) {
            // this token is blacklisted.
            return new HashMap<>();
        }
        JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec);

        final String withoutSignature = substringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
    }

    @Override
    public void invalidate(JwtToken token) {
        tokenManager.addInvalidToken(token);
    }

    private JwtToken newToken(Map<String, String> attributes, int expirationSec) {
        DateTime now = dateService.now();
        final Claims claims = Jwts.claims()
                .setIssuer(issuer)
                .setIssuedAt(now.toDate());

        DateTime expiresAt = null;

        if(expirationSec > 0) {
            expiresAt = now.plusSeconds(expirationSec);
            claims.setExpiration(expiresAt.toDate());
        }

        claims.putAll(attributes);

        String token = Jwts
                    .builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compressWith(COMPRESSION_CODEC)
                    .compact();

        Long expirationMillis = expiresAt == null ? -1 : expiresAt.getMillis();

        return new JwtToken(
                token,
                expirationMillis
        );
    }

    private static Map<String, String> parseClaims(Supplier<Claims> toClaims) {
        try {
            Claims claims = toClaims.get();
            Map<String, String> result = new HashMap<>();

            claims.forEach((key, value) -> result.put(key, String.valueOf(value)));
            return result;
        }
        catch(IllegalArgumentException | JwtException e) {
            return new HashMap<>();
        }
    }

    @Override
    public Date now() {
        return dateService.now().toDate();
    }
}
