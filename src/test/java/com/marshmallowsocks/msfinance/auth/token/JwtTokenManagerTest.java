package com.marshmallowsocks.msfinance.auth.token;

import com.marshmallowsocks.msfinance.auth.token.model.JwtToken;
import com.marshmallowsocks.msfinance.auth.token.manager.JwtTokenManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class JwtTokenManagerTest {

    @Mock
    private JwtToken token;

    private JwtTokenManager jwtTokenManager;

    @Before
    public void setup() {

        when(token.getToken()).thenReturn("A token");
        when(token.getExpirationMillis()).thenReturn(1000L);
        when(token.isExpired()).thenCallRealMethod();
        jwtTokenManager = new JwtTokenManager();
        jwtTokenManager.addInvalidToken(token);
    }

    @Test
    public void addToken_invalidate_shouldContainInvalidated() {
        // arrange
        JwtToken expiredToken = new JwtToken("A token", 500L);

        // act
        jwtTokenManager.addInvalidToken(expiredToken);

        // assert
        Set<JwtToken> invalidTokens = jwtTokenManager.getInvalidTokens();

        for(JwtToken t : invalidTokens) {
            Assert.assertEquals(t.getToken(), expiredToken.getToken());
            Assert.assertEquals(t.getExpirationMillis(), expiredToken.getExpirationMillis());
        }
    }

    @Test
    public void addLongerLivingToken_invalidate_shouldContainShorterToken() {
        // arrange
        JwtToken expiredToken = new JwtToken("A token", 200000L);

        // act
        jwtTokenManager.addInvalidToken(expiredToken);

        // assert
        Set<JwtToken> invalidTokens = jwtTokenManager.getInvalidTokens();

        for(JwtToken t : invalidTokens) {
            Assert.assertEquals(t.getToken(), token.getToken());
            Assert.assertEquals(t.getExpirationMillis(), token.getExpirationMillis());
        }
    }

    @Test
    public void invalidToken_isInvalid() {
        // arrange
        JwtToken invalidToken = new JwtToken("A token", 1000L);

        // act
        jwtTokenManager.addInvalidToken(invalidToken);

        // assert
        Assert.assertTrue(jwtTokenManager.isInvalid("A token"));
    }

    @Test
    public void invalidToken_shouldClear() {

        // assert
        Assert.assertTrue(!jwtTokenManager.getInvalidTokens().isEmpty());

        // act
        jwtTokenManager.clearInvalidTokens();

        // assert
        verify(token, times(1)).isExpired();
        Assert.assertTrue(jwtTokenManager.getInvalidTokens().isEmpty());
    }
}
