package com.marshmallowsocks.msfinance.user.controller;

import com.marshmallowsocks.msfinance.auth.service.AuthenticationService;
import com.marshmallowsocks.msfinance.auth.token.JwtToken;
import com.marshmallowsocks.msfinance.user.service.UserService;
import com.marshmallowsocks.msfinance.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public JwtToken register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        mongoUserService
            .save(
                new User.Builder()
                    .withUserName(username)
                    .withPassword(password)
                    .build()
                );

        return login(username, password);
    }

    @PostMapping("/login")
    public JwtToken login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        return authenticationService
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }
}
