package com.marshmallowsocks.msfinance.auth.service;

import com.marshmallowsocks.msfinance.auth.token.model.JwtToken;
import com.marshmallowsocks.msfinance.auth.token.TokenService;
import com.marshmallowsocks.msfinance.user.model.User;
import com.marshmallowsocks.msfinance.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TokenAuthenticationService implements AuthenticationService {

    private final TokenService jwtTokenService;
    private final UserService mongoUserService;

    @Autowired
    public TokenAuthenticationService(TokenService jwtTokenService, UserService mongoUserService) {
        this.jwtTokenService = jwtTokenService;
        this.mongoUserService = mongoUserService;
    }

    @Override
    public Optional<JwtToken> login(String userName, String password) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("username", userName);

        return mongoUserService
                .findByUserName(userName)
                .filter(user -> passwordVerify(password, user.getPassword()))
                .map(user -> jwtTokenService.expiring(attributes));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional
                .of(jwtTokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(mongoUserService::findByUserName);
    }

    @Override
    public void logout(String token) {
        // set token to expire 30 minutes from now.
        // all web tokens expire in 30 minutes.
        jwtTokenService.invalidate(
                new JwtToken(token, System.currentTimeMillis() + (1800 * 1000)
        ));
    }


}
