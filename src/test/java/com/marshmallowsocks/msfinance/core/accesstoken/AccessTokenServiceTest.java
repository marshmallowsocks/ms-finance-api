package com.marshmallowsocks.msfinance.core.accesstoken;

import com.marshmallowsocks.msfinance.core.MSPlaidClient;
import com.marshmallowsocks.msfinance.core.ServiceBaseTest;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessTokenRepository;
import com.marshmallowsocks.msfinance.testutils.ModelObjectFactory;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import io.leangen.graphql.execution.ResolutionEnvironment;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AccessTokenServiceTest extends ServiceBaseTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private MSPlaidClient msPlaidClient;

    private Response<ItemPublicTokenExchangeResponse> response;

    @InjectMocks
    private AccessTokenService accessTokenService;

    @Before
    public void setup() {
        super.init();
        List<AccessToken> accessTokens = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            accessTokens.add(ModelObjectFactory.randomAccessToken());
        }

        when(accessTokenRepository.findByUserId("1234")).thenReturn(accessTokens);

        ItemPublicTokenExchangeResponse accessTokenResponse = new ItemPublicTokenExchangeResponse();
        ReflectionTestUtils.setField(accessTokenResponse, "accessToken", "access token");
        ReflectionTestUtils.setField(accessTokenResponse, "itemId", "item token");
        response = Response.success(accessTokenResponse);
    }

    @Test
    public void fetchAll_shouldNotReturnUserId() {

        // act
        List<AccessToken> result = accessTokenService.fetchAll(environment);

        // assert
        verify(accessTokenRepository, times(1)).findByUserId(anyString());
        result.forEach(a -> Assert.assertNull(a.getUserId()));
    }

    @Test
    public void exchangePublicToken_success_shouldReturnAccessToken() {

        // arrange
        when(msPlaidClient.exchangePublicToken(anyString())).thenReturn(response);
        AccessToken expectedToken = new AccessToken("access token", "item token", "1234");

        // act
        ItemPublicTokenExchangeResponse accessToken = accessTokenService.exchangePublicToken(environment, "public token");

        // assert
        verify(accessTokenRepository, times(1)).save(expectedToken);
        Assert.assertEquals(accessToken.getAccessToken(), "access token");
        Assert.assertEquals(accessToken.getItemId(), "item token");
    }

    @Test
    public void exchangeAccessToken_fail_shouldReturnNull() {
        response = Response.error(500, error());

        when(msPlaidClient.exchangePublicToken(anyString())).thenReturn(response);
        ItemPublicTokenExchangeResponse accessTokenResponse = accessTokenService.exchangePublicToken(environment, "public token");

        Assert.assertNull(accessTokenResponse);
    }
}
