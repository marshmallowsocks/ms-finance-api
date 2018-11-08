package com.marshmallowsocks.msfinance.token;

import com.marshmallowsocks.msfinance.auth.token.JwtTokenManager;
import com.marshmallowsocks.msfinance.auth.token.JwtTokenService;
import com.marshmallowsocks.msfinance.auth.token.model.JwtToken;
import com.marshmallowsocks.msfinance.util.DateService;
import com.marshmallowsocks.msfinance.util.JodaDateService;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class JwtTokenServiceTest {

    private DateService dateService;

    @Mock
    private JwtTokenManager tokenManager;

    private JwtTokenService jwtTokenService;

    Map<String, String> attributes;

    @Before
    public void setup() {
        dateService = new JodaDateService(DateTimeZone.UTC);
        jwtTokenService = new JwtTokenService(
                dateService,
                tokenManager,
                "issuer",
                2,
                1,
                "secret"
        );

        when(tokenManager.isInvalid(anyString())).thenReturn(false);
        attributes = new HashMap<>();
        attributes.put("username", "user");
    }

    @Test
    public void getPermanentToken_cannotExpire() {

        // act
        JwtToken token = jwtTokenService.permanent(attributes);

        // assert
        Assert.assertTrue(token.isPermanent());
        Assert.assertFalse(token.isExpired());
    }

    @Test
    public void getExpiringToken_canExpire() throws InterruptedException {

        // act
        JwtToken token = jwtTokenService.expiring(attributes);

        // assert
        Assert.assertFalse(token.isPermanent());
        Assert.assertFalse(token.isExpired());

        Thread.sleep(2 * 1000);
        Assert.assertTrue(token.isExpired());
    }

    @Test
    public void validToken_verifyTrue() {
        // arrange
        JwtToken token = jwtTokenService.expiring(attributes);

        // act
        Map<String, String> claims = jwtTokenService.verify(token.getToken());

        // assert
        Assert.assertFalse(claims.isEmpty());
        Assert.assertEquals(claims.get("username"), "user");
    }

    @Test
    public void invalidToken_verifyFalse() {
        // arrange
        JwtToken token = jwtTokenService.expiring(attributes);
        when(tokenManager.isInvalid(token.getToken())).thenReturn(true);
        // act
        Map<String, String> claims = jwtTokenService.verify(token.getToken());

        // assert
        Assert.assertTrue(claims.isEmpty());
    }

    @Test
    public void validToken_Verify_invalidate_Verify() {
        // arrange
        // use a permanent token to ensure it's not expired
        JwtToken token = jwtTokenService.permanent(attributes);
        when(tokenManager.isInvalid(token.getToken())).thenReturn(false);
        doAnswer(invocation -> {
            when(tokenManager.isInvalid(token.getToken())).thenReturn(true);
            return null;
        }).when(tokenManager).addInvalidToken(any(JwtToken.class));

        // act
        Map<String, String> claims = jwtTokenService.verify(token.getToken());

        // assert
        Assert.assertFalse(claims.isEmpty());
        Assert.assertEquals(claims.get("username"), "user");

        // act again
        jwtTokenService.invalidate(token);
        claims = jwtTokenService.verify(token.getToken());

        // assert again
        Assert.assertTrue(claims.isEmpty());
    }
}
