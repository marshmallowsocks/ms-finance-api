package com.marshmallowsocks.msfinance.auth;

import com.marshmallowsocks.msfinance.auth.service.TokenAuthenticationService;
import com.marshmallowsocks.msfinance.auth.token.JwtToken;
import com.marshmallowsocks.msfinance.auth.token.TokenService;
import com.marshmallowsocks.msfinance.user.model.User;
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
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TokenAuthenticationServiceTest {

    @Mock
    private TokenService jwtTokenService;

    @Mock
    private UserService mongoUserService;

    @Mock
    private JwtToken jwtToken;

    @Mock
    private User user;

    @InjectMocks
    private TokenAuthenticationService tokenAuthenticationService;

    @Before
    public void setup() {
        when(jwtToken.getToken()).thenReturn("A valid token");
        when(jwtToken.getExpirationMillis()).thenReturn(1000L);
        when(jwtTokenService.expiring(any(Map.class)))
                .thenReturn(jwtToken);

//        when(user.getUsername()).thenReturn("A user");
        when(user.getPassword()).thenReturn(BCrypt.hashpw("A password", BCrypt.gensalt()));
        when(mongoUserService.findByUserName("A user"))
                .thenReturn(Optional.of(user));
    }

    @Test
    public void login_validCredentials_shouldReturnToken() {
        // act
        Optional<JwtToken> tokenOptional = tokenAuthenticationService.login("A user", "A password");
        JwtToken token;

        // assert
        Assert.assertTrue(tokenOptional.isPresent());
        token = tokenOptional.get();

        verify(mongoUserService, times(1)).findByUserName("A user");
        assertEquals(token.getToken(), jwtToken.getToken());
        assertEquals(token.getExpirationMillis(), jwtToken.getExpirationMillis());
    }

    @Test
    public void login_invalidCredentials_shouldReturnEmpty() {
        // act
        Optional<JwtToken> tokenOptional = tokenAuthenticationService.login("A user", "A wrong password");

        // assert
        assertEquals(tokenOptional, Optional.empty());
    }
}
