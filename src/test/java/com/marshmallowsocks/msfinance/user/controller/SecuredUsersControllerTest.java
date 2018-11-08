package com.marshmallowsocks.msfinance.user.controller;

import com.marshmallowsocks.msfinance.auth.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SecuredUsersControllerTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SecuredUsersController securedUsersController;

    private static final String AUTHORIZATION = "Authorization";

    @Before
    public void setup() {
        doNothing().when(authenticationService).logout(anyString());
        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer token");
    }

    @Test
    public void logout_goodCredentials_alwaysSuccess() {
        // act and assert 
        assertTrue(securedUsersController.logout(request));
    }

    @Test(expected = BadCredentialsException.class)
    public void logout_badCredentials_throwsException() {
        // arrange
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // act
        securedUsersController.logout(request);
    }
}
