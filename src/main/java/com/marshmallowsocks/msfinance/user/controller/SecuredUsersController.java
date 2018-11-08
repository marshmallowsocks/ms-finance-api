package com.marshmallowsocks.msfinance.user.controller;

import com.marshmallowsocks.msfinance.auth.service.AuthenticationService;
import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/users")
final class SecuredUsersController {

    private AuthenticationService authenticationService;
    private static final String BEARER = "Bearer";

    @Autowired
    public SecuredUsersController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/logout")
    boolean logout(HttpServletRequest request) {
        final String param = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .orElse(request.getParameter("t"));

        final String token = Optional.ofNullable(param)
                .map(value -> removeStart(value, BEARER))
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

        authenticationService.logout(token);
        return true;
    }
}
