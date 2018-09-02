package com.marshmallowsocks.msfinance.data.accesstoken;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {

    Optional<AccessToken> findByItemToken(String itemToken);
    List<AccessToken> findByUserId(String userId);
}
