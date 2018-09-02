package com.marshmallowsocks.msfinance.user.controller;

import com.marshmallowsocks.msfinance.auth.service.AuthenticationService;
import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
final class SecuredUsersController {

    private AuthenticationService authenticationService;

    @Autowired
    public SecuredUsersController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/current")
    User getCurrent(@AuthenticationPrincipal final User user) {
        // do not return the id and password, obviously
        return new User.Builder()
                .withUserName(user.getUsername())
                .build();
    }

    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final User user) {
        authenticationService.logout(user);
        return true;
    }
}
