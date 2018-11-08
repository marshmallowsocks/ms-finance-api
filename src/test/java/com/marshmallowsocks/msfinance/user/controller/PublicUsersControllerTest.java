package com.marshmallowsocks.msfinance.user.controller;

import com.marshmallowsocks.msfinance.auth.service.AuthenticationService;
import com.marshmallowsocks.msfinance.auth.token.model.JwtToken;
import com.marshmallowsocks.msfinance.user.model.User;
import com.marshmallowsocks.msfinance.user.model.UserRequest;
import com.marshmallowsocks.msfinance.user.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PublicUsersControllerTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;

    @InjectMocks
    private PublicUsersController publicUsersController;

    private UserRequest userRequest;
    @Before
    public void setup() {
        JwtToken token = new JwtToken("token", 1000L);

        when(userService.save(any(User.class))).thenReturn(null);
        when(authenticationService.login(eq("user"), eq("pass")))
                .thenReturn(Optional.of(token));
        when(authenticationService.login(eq("user"), eq("bad-pass")))
                .thenReturn(Optional.empty());

        userRequest = new UserRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("pass");
    }

    @Test
    public void register_alwaysReturnToken() {
        // act
        JwtToken token = publicUsersController.register(userRequest);

        // assert
        Assert.assertEquals(token.getToken(), "token");
    }

    @Test
    public void login_goodCredentials_success() {
        // act
        JwtToken token = publicUsersController.login(userRequest);

        // assert
        Assert.assertEquals(token.getToken(), "token");
    }

    @Test(expected = BadCredentialsException.class)
    public void login_badCredentials_failure() {
        // arrange
        userRequest.setPassword("bad-pass");

        // act
        JwtToken token = publicUsersController.login(userRequest);
    }
}
