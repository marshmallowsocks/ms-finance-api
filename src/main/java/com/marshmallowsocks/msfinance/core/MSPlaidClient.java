package com.marshmallowsocks.msfinance.core;

import com.marshmallowsocks.msfinance.config.autoconfiguration.core.PlaidConfiguration;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.TransactionsGetResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;

@Component
@EnableConfigurationProperties
public class MSPlaidClient {

    private PlaidClient client;

    public MSPlaidClient() {}

    @Autowired
    public MSPlaidClient(PlaidConfiguration plaidConfiguration) {
        client = PlaidClient
                .newBuilder()
                .clientIdAndSecret(plaidConfiguration.getClientId(), plaidConfiguration.getClientSecret())
                .publicKey(plaidConfiguration.getPublicKey())
                .developmentBaseUrl()
                .build();
    }

    public PlaidClient client() {
        return client;
    }

    public Response<ItemPublicTokenExchangeResponse> exchangePublicToken(String publicToken) {
        try {
            return client.service()
                .itemPublicTokenExchange(
                        new ItemPublicTokenExchangeRequest(publicToken)
                ).execute();
        } catch (IOException e) {
            return null;
        }
    }

    public Response<TransactionsGetResponse> getTransactionsFor(String accessToken) {
        DateTime start = DateTime.now()
                .withTimeAtStartOfDay()
                .withDayOfYear(1);
        DateTime end = DateTime.now();

        try {
            return client.service()
                    .transactionsGet(
                            new TransactionsGetRequest(
                                    accessToken,
                                    start.toDate(),
                                    end.toDate()
                            )
                    ).execute();
        } catch (IOException e) {
            return null;
        }
    }

}
