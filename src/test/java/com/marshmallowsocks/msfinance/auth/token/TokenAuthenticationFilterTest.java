package com.marshmallowsocks.msfinance.auth.token;

import com.marshmallowsocks.msfinance.auth.token.filter.TokenAuthenticationFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TokenAuthenticationFilterTest {

    @Mock
    private RequestMatcher requestMatcher;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    private static final String AUTHORIZATION = "Authorization";

    @Before
    public void setup() {
        tokenAuthenticationFilter = new TokenAuthenticationFilter(requestMatcher);
        tokenAuthenticationFilter.setAuthenticationManager(authenticationManager);

        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer token");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("token");
    }

    @Test
    public void attemptAuthentication_success() throws IOException, ServletException {
        // act
        Authentication authentication = tokenAuthenticationFilter.attemptAuthentication(request, response);

        // assert
        Assert.assertEquals(authentication.getCredentials(), "token");
    }

    @Test(expected = BadCredentialsException.class)
    public void attemptAuthentication_noToken_throwsException() throws IOException, ServletException {
        // arrange
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // act
        tokenAuthenticationFilter.attemptAuthentication(request, response);
    }
}
