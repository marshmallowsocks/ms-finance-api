package com.marshmallowsocks.msfinance.core;

import com.marshmallowsocks.msfinance.config.autoconfiguration.core.PlaidConfiguration;
import com.plaid.client.PlaidApiService;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MSPlaidClientTest extends ServiceBaseTest {

    private MSPlaidClient msPlaidClient;

    @Mock
    private PlaidApiService service;

    @Mock
    private Call<ItemPublicTokenExchangeResponse> request;

    @Before
    public void setup() throws IOException {
        PlaidConfiguration plaidConfiguration = new PlaidConfiguration();
        plaidConfiguration.setClientId("client-id");
        plaidConfiguration.setClientSecret("client-secret");
        plaidConfiguration.setPublicKey("public-key");

        msPlaidClient = new MSPlaidClient(plaidConfiguration);

        ItemPublicTokenExchangeResponse accessTokenResponse = new ItemPublicTokenExchangeResponse();
        ReflectionTestUtils.setField(accessTokenResponse, "accessToken", "access token");
        ReflectionTestUtils.setField(accessTokenResponse, "itemId", "item token");

        when(service.itemPublicTokenExchange(any(ItemPublicTokenExchangeRequest.class)))
                .thenReturn(request);
        when(request.execute())
                .thenReturn(Response.success(accessTokenResponse));
    }

    @Test
    public void getClient_returnPlaidClient() {

        // act
        PlaidClient expectedClient = msPlaidClient.client();

        // assert
        Assert.assertThat(expectedClient, IsInstanceOf.instanceOf(PlaidClient.class));
    }

    @Test
    public void exchangePublicToken_valid() {

        // arrange
        PlaidClient client = msPlaidClient.client();
        ReflectionTestUtils.setField(client, "plaidApiService", service);

        // act
        Response<ItemPublicTokenExchangeResponse> result = msPlaidClient.exchangePublicToken("public token");

        // assert
        Assert.assertThat(result.body(), IsNull.notNullValue());
        Assert.assertEquals(result.body().getAccessToken(), "access token");
        Assert.assertEquals(result.body().getItemId(), "item token");
    }

    @Test
    public void exchangePublicToken_invalid() throws IOException {

        // arrange
        PlaidClient client = msPlaidClient.client();
        ReflectionTestUtils.setField(client, "plaidApiService", service);

        when(request.execute())
                .thenThrow(new IOException());

        // act
        Response<ItemPublicTokenExchangeResponse> result = msPlaidClient.exchangePublicToken("public token");

        // assert
        Assert.assertNull(result);
    }
}
