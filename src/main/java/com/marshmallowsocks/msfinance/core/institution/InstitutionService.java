package com.marshmallowsocks.msfinance.core.institution;

import com.marshmallowsocks.msfinance.core.MSPlaidClient;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessTokenRepository;
import com.marshmallowsocks.msfinance.data.institutions.Institution;
import com.marshmallowsocks.msfinance.data.institutions.InstitutionRepository;
import com.plaid.client.response.TransactionsGetResponse;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    private InstitutionRepository institutionRepository;
    private AccessTokenRepository accessTokenRepository;
    private MSPlaidClient msPlaidClient;
    private String userId;

    private static Logger LOGGER = LoggerFactory.getLogger(InstitutionService.class);

    @Autowired
    public InstitutionService(InstitutionRepository institutionRepository, AccessTokenRepository accessTokenRepository, MSPlaidClient msPlaidClient) {
        this.institutionRepository = institutionRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.msPlaidClient = msPlaidClient;
    }

    @GraphQLQuery(name = "institutions")
    public List<Institution> getAllInstitutions(@GraphQLEnvironment ResolutionEnvironment environment) {
        userId = environment.rootContext.toString();
        return institutionRepository.getInstitutionsForUserId(userId);
    }

    @GraphQLQuery(name = "transactionsForItem")
    public TransactionsGetResponse getTransactionsForItem(String itemToken) {
        LOGGER.info("getTransactionsForItem invoked for itemToken:" + itemToken);
        Optional<AccessToken> token = accessTokenRepository.findByItemToken(itemToken);
        if(token.isPresent()) {
            AccessToken t = token.get();
            Response<TransactionsGetResponse> response = msPlaidClient.getTransactionsFor(t.getAccessToken());
            if(response.isSuccessful()) {
                return response.body();
            }
        }
        LOGGER.warn("Token was not found.");
        return null;
    }
}
