package com.marshmallowsocks.msfinance.user.service;

import com.marshmallowsocks.msfinance.user.model.User;

import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> find(String id);
    Optional<User> findByUserName(String userName);
}
