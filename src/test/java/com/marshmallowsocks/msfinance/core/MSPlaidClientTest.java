package com.marshmallowsocks.msfinance.core;

import com.marshmallowsocks.msfinance.config.autoconfiguration.core.PlaidConfiguration;
import com.plaid.client.PlaidApiService;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.TransactionsGetResponse;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MSPlaidClientTest extends ServiceBaseTest {

    private MSPlaidClient msPlaidClient;

    @Mock
    private PlaidApiService service;

    @Mock
    private Call<ItemPublicTokenExchangeResponse> accessTokenRequest;

    @Mock
    private Call<TransactionsGetResponse> transactionsGetRequest;

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

        // too much hassle to actually set anything up
        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();

        when(service.itemPublicTokenExchange(any(ItemPublicTokenExchangeRequest.class)))
                .thenReturn(accessTokenRequest);
        when(service.transactionsGet(any(TransactionsGetRequest.class)))
                .thenReturn(transactionsGetRequest);

        when(accessTokenRequest.execute())
                .thenReturn(Response.success(accessTokenResponse));
        when(transactionsGetRequest.execute())
                .thenReturn(Response.success(transactionsGetResponse));
    }

    @Test
    public void getClient_returnPlaidClient() {

        // act
        PlaidClient expectedClient = msPlaidClient.client();

        // assert
        Assert.assertThat(expectedClient, IsInstanceOf.instanceOf(PlaidClient.class));
    }

    @Test
    public void exchangePublicToken_valid() throws IOException {

        // arrange
        PlaidClient client = msPlaidClient.client();
        ReflectionTestUtils.setField(client, "plaidApiService", service);

        // act
        Response<ItemPublicTokenExchangeResponse> result = msPlaidClient.exchangePublicToken("public token");

        // assert
        verify(service, times(1))
                .itemPublicTokenExchange(any(ItemPublicTokenExchangeRequest.class));
        verify(accessTokenRequest, times(1)).execute();
        Assert.assertThat(result.body(), IsNull.notNullValue());
        Assert.assertEquals(result.body().getAccessToken(), "access token");
        Assert.assertEquals(result.body().getItemId(), "item token");
    }

    @Test
    public void exchangePublicToken_invalid() throws IOException {

        // arrange
        PlaidClient client = msPlaidClient.client();
        ReflectionTestUtils.setField(client, "plaidApiService", service);

        when(accessTokenRequest.execute())
                .thenThrow(new IOException());

        // act
        Response<ItemPublicTokenExchangeResponse> result = msPlaidClient.exchangePublicToken("public token");

        // assert
        verify(service, times(1))
                .itemPublicTokenExchange(any(ItemPublicTokenExchangeRequest.class));
        verify(accessTokenRequest, times(1)).execute();
        Assert.assertNull(result);
    }

    @Test
    public void getTransactionsFor_valid() throws IOException {

        // arrange
        PlaidClient client = msPlaidClient.client();
        ReflectionTestUtils.setField(client, "plaidApiService", service);

        // act
        Response<TransactionsGetResponse> result = msPlaidClient.getTransactionsFor("access token");

        // assert
        verify(service, times(1))
                .transactionsGet(any(TransactionsGetRequest.class));
        verify(transactionsGetRequest, times(1)).execute();
        Assert.assertThat(result.body(), IsInstanceOf.instanceOf(TransactionsGetResponse.class));
    }
}
