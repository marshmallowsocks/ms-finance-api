package com.marshmallowsocks.msfinance.user.controller;

import com.marshmallowsocks.msfinance.auth.service.AuthenticationService;
import com.marshmallowsocks.msfinance.auth.token.JwtToken;
import com.marshmallowsocks.msfinance.user.model.UserRequest;
import com.marshmallowsocks.msfinance.user.service.UserService;
import com.marshmallowsocks.msfinance.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/users")
public class PublicUsersController {

    private final AuthenticationService authenticationService;
    private final UserService mongoUserService;

    @Autowired
    public PublicUsersController(AuthenticationService authenticationService, UserService mongoUserService) {
        this.authenticationService = authenticationService;
        this.mongoUserService = mongoUserService;
    }

    @PostMapping("/register")
    public JwtToken register(@RequestBody UserRequest userRequest) {
        mongoUserService
            .save(
                new User.Builder()
                    .withUserName(userRequest.getUsername())
                    .withPassword(userRequest.getPassword())
                    .build()
                );

        return login(userRequest);
    }

    @PostMapping("/login")
    public JwtToken login(@RequestBody UserRequest userRequest) {
        return authenticationService
                .login(userRequest.getUsername(), userRequest.getPassword())
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }
}
