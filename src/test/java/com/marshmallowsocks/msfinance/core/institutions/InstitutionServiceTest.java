package com.marshmallowsocks.msfinance.core.institutions;

import com.marshmallowsocks.msfinance.core.MSPlaidClient;
import com.marshmallowsocks.msfinance.core.ServiceBaseTest;
import com.marshmallowsocks.msfinance.core.institution.InstitutionService;
import com.marshmallowsocks.msfinance.data.accesstoken.AccessTokenRepository;
import com.marshmallowsocks.msfinance.data.institutions.Institution;
import com.marshmallowsocks.msfinance.data.institutions.InstitutionRepository;
import com.marshmallowsocks.msfinance.testutils.ModelObjectFactory;
import com.plaid.client.response.TransactionsGetResponse;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class InstitutionServiceTest extends ServiceBaseTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private MSPlaidClient msPlaidClient;

    private Response<TransactionsGetResponse> response;

    @InjectMocks
    private InstitutionService institutionService;

    @Before
    public void setup() {
        super.init();
        List<Institution> institutions = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            institutions.add(ModelObjectFactory.randomInstitution());
        }

        response = Response.success(new TransactionsGetResponse());

        when(accessTokenRepository.findByItemToken(anyString()))
                .thenReturn(Optional.of(ModelObjectFactory.randomAccessToken()));
        when(msPlaidClient.getTransactionsFor(anyString()))
                .thenReturn(response);
        when(institutionRepository.getInstitutionsForUserId(anyString()))
                .thenReturn(institutions);
    }

    @Test
    public void getAllInstitutions_returnsInstitution() {

        // act
        List<Institution> institutions = institutionService.getAllInstitutions(environment);

        // assert
        verify(institutionRepository, times(1)).getInstitutionsForUserId(anyString());
        institutions.forEach(institution -> Assert.assertThat(institution, IsInstanceOf.instanceOf(Institution.class)));
    }

    @Test
    public void getTransactionsForItem_valid() {

        // act
        TransactionsGetResponse transactionsGetResponse = institutionService.getTransactionsForItem("item token");

        // assert
        verify(msPlaidClient, times(1)).getTransactionsFor(anyString());
        Assert.assertNotNull(transactionsGetResponse);
    }

    @Test
    public void getTransactionsForItem_invalid() {

        // arrange
        when(accessTokenRepository.findByItemToken(anyString()))
                .thenReturn(Optional.empty());

        // act
        TransactionsGetResponse transactionsGetResponse = institutionService.getTransactionsForItem("item token");

        // assert
        verify(msPlaidClient, never()).getTransactionsFor(anyString());
        Assert.assertNull(transactionsGetResponse);
    }
}
