package com.marshmallowsocks.msfinance.auth;

import com.marshmallowsocks.msfinance.auth.service.TokenAuthenticationService;
import com.marshmallowsocks.msfinance.auth.token.JwtToken;
import com.marshmallowsocks.msfinance.auth.token.JwtTokenManager;
import com.marshmallowsocks.msfinance.auth.token.TokenService;
import com.marshmallowsocks.msfinance.user.model.User;
import com.marshmallowsocks.msfinance.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @Spy
    private JwtTokenManager jwtTokenManager;

    @InjectMocks
    private TokenAuthenticationService tokenAuthenticationService;

    @Before
    public void setup() {
        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("username", "A user");

        when(jwtToken.getToken()).thenReturn("A valid token");
        when(jwtToken.getExpirationMillis()).thenReturn(1000L);
        when(jwtTokenService.verify("A valid token"))
                .thenReturn(userAttributes);
        when(jwtTokenService.expiring(any(Map.class)))
                .thenReturn(jwtToken);

        doCallRealMethod().when(jwtTokenService)
                .invalidate(any(JwtToken.class));

        when(user.getUsername()).thenReturn("A user");
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
        assertTrue(tokenOptional.isPresent());
        token = tokenOptional.get();

        verify(mongoUserService, times(1)).findByUserName(eq("A user"));
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

    @Test
    public void findUserByToken_userExists() {
        // act
        Optional<User> userOptional = tokenAuthenticationService.findByToken("A valid token");
        User user;

        // assert
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        verify(jwtTokenService, times(1)).verify(eq("A valid token"));
        assertEquals(user.getUsername(), "A user");
        assertEquals(user.getPassword(), BCrypt.hashpw("A password", BCrypt.gensalt()));
    }

    @Test
    public void invaliateToken_tokenFetchFails() {
        // act
        tokenAuthenticationService.logout("A valid token");

        // assert
        assertTrue(jwtTokenManager.isInvalid("A valid token"));
    }
}
