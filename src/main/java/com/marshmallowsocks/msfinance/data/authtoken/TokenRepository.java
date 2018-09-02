package com.marshmallowsocks.msfinance.data.authtoken;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<InvalidToken, String> {
    public Optional<InvalidToken> findByToken(String token);
}
