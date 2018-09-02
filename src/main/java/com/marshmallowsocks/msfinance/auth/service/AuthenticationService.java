package com.marshmallowsocks.msfinance.auth.service;

import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

public interface AuthenticationService {
    Optional<String> login(String userName, String password);
    Optional<User> findByToken(String token);
    void logout(User user);

    default boolean passwordVerify(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
