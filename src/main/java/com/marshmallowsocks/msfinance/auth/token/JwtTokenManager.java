package com.marshmallowsocks.msfinance.auth.token;

import com.marshmallowsocks.msfinance.auth.token.model.JwtToken;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenManager {

    private Set<JwtToken> invalidTokens;

    public JwtTokenManager() {
        invalidTokens = new HashSet<>();
    }

    public Set<JwtToken> getInvalidTokens() {
        return invalidTokens;
    }

    public void addInvalidToken(JwtToken token) {
        // set the token to the one with the least
        // expiration time.
        List<JwtToken> maybeTokens = invalidTokens
            .stream()
            .filter(jwtToken -> jwtToken.getToken().equals(token.getToken()))
            .collect(Collectors.toList());

        Optional<JwtToken> finalMaybeToken = maybeTokens
                .stream()
                .min(Comparator.comparingLong(JwtToken::getExpirationMillis));

        if(finalMaybeToken.isPresent()) {
            JwtToken finalToken = finalMaybeToken.get();
            invalidTokens.removeAll(maybeTokens);
            if(finalToken.getExpirationMillis() < token.getExpirationMillis()) {
                invalidTokens.add(finalToken);
            }
            else {
                invalidTokens.add(token);
            }
        }

        else {
            invalidTokens.add(token);
        }

        // nothing to do, min returns null on empty set.
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isInvalid(String token) {
        return invalidTokens
            .stream()
            .anyMatch(jwtToken -> jwtToken.getToken().equals(token));
    }

    @Scheduled(fixedRate = 60000)
    public void clearInvalidTokens() {
        // runs every minute,
        // clears out blacklisted tokens

        invalidTokens.removeAll(
                invalidTokens
                .stream()
                .filter(JwtToken::isExpired)
                .collect(Collectors.toSet())
        );
    }
}
