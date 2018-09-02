package com.marshmallowsocks.msfinance.auth.token;

import com.marshmallowsocks.msfinance.data.authtoken.InvalidToken;
import com.marshmallowsocks.msfinance.data.authtoken.TokenRepository;
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

    @Autowired
    private TokenRepository tokenRepository;

    public JwtTokenService(
            final DateService dateService,
            @Value("${jwt.issuer:ms-finance}") String issuer,
            @Value("${jwt.expiration-sec:1800}") int expirationSec,
            @Value("${jwt.clock-skew-sec:300}") int clockSkewSec,
            @Value("${jwt.secret}") String secretKey) {
        this.dateService = dateService;
        this.issuer = issuer;
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;
        this.secretKey = secretKey;
    }

    @Override
    public String permanent(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiring(Map<String, String> attributes) {
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
        if(tokenRepository.findByToken(token).isPresent()) {
            // the authtoken is invalid.
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
    public void invalidate(String token) {
        InvalidToken tokenModel = new InvalidToken(token);
        tokenRepository.save(tokenModel);
    }

    private String newToken(Map<String, String> attributes, int expirationSec) {
        DateTime now = dateService.now();
        final Claims claims = Jwts.claims()
                .setIssuer(issuer)
                .setIssuedAt(now.toDate());

        if(expirationSec > 0) {
            DateTime expiresAt = now.plusSeconds(expirationSec);
            claims.setExpiration(expiresAt.toDate());
        }

        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
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
