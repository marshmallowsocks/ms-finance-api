package com.marshmallowsocks.msfinance.core.accesstoken;

import com.marshmallowsocks.msfinance.core.MSPlaidClient;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessTokenRepository;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessTokenService {

    private String userId;
    private AccessTokenRepository accessTokenRepository;
    private MSPlaidClient msPlaidClient;

    @Autowired
    public AccessTokenService(
            AccessTokenRepository accessTokenRepository,
            MSPlaidClient msPlaidClient) {
        this.accessTokenRepository = accessTokenRepository;
        this.msPlaidClient = msPlaidClient;
    }

    @GraphQLQuery(name = "accessTokens")
    public List<AccessToken> fetchAll(@GraphQLEnvironment ResolutionEnvironment environment) {
        userId = environment.rootContext.toString();
        List<AccessToken> accessTokens = accessTokenRepository.findByUserId(userId);
        return accessTokens
                .stream()
                .peek(accessToken -> accessToken.setUserId(null))
                .collect(Collectors.toList());
    }

    @GraphQLQuery(name = "exchangePublicToken")
    public ItemPublicTokenExchangeResponse exchangePublicToken(@GraphQLEnvironment ResolutionEnvironment environment, String publicToken) {
        Response<ItemPublicTokenExchangeResponse> response = msPlaidClient.exchangePublicToken(publicToken);
        userId = environment.rootContext.toString();

        if(response.isSuccessful()) {
            ItemPublicTokenExchangeResponse body = response.body();
            AccessToken accessToken = new AccessToken(
                    body.getAccessToken(),
                    body.getItemId(),
                    userId
            );

            accessTokenRepository.save(accessToken);
            return body;
        }

        return null;
    }
}
