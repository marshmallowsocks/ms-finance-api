package com.marshmallowsocks.msfinance.core.accesstoken;

import com.marshmallowsocks.msfinance.core.MSPlaidClient;
import com.marshmallowsocks.msfinance.core.ServiceBase;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessTokenRepository;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessTokenService implements ServiceBase {

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


    public List<AccessToken> fetchAll() {
        List<AccessToken> accessTokens = accessTokenRepository.findByUserId(getUserId());
        return accessTokens
                .stream()
                .peek(accessToken -> accessToken.userId = null)
                .collect(Collectors.toList());
    }

    public ItemPublicTokenExchangeResponse exchangePublicToken(String publicToken) {
        Response<ItemPublicTokenExchangeResponse> response = msPlaidClient.exchangePublicToken(publicToken);

        if(response.isSuccessful()) {
            ItemPublicTokenExchangeResponse body = response.body();
            AccessToken accessToken = new AccessToken(
                    body.getAccessToken(),
                    body.getItemId(),
                    getUserId()
            );

            accessTokenRepository.save(accessToken);
            return body;
        }

        return null;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
