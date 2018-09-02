package com.marshmallowsocks.msfinance.data.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    public Optional<UserModel> findByUserName(String userName);
}
