package com.marshmallowsocks.msfinance.core.institution;

import com.marshmallowsocks.msfinance.data.institutions.Institution;
import com.marshmallowsocks.msfinance.user.model.User;
import com.plaid.client.response.TransactionsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/institutions", consumes = MediaType.APPLICATION_JSON_VALUE)
public class InstitutionController {

    private InstitutionService institutionService;

    @Autowired
    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping("/all")
    public List<Institution> getAllInstitutions(@AuthenticationPrincipal final User user) {
        institutionService.setUserId(user.getId());
        return institutionService.getAllInstitutions();
    }

    @PostMapping("/transactions")
    public TransactionsGetResponse getTransactions(@AuthenticationPrincipal final User user, @RequestBody GetTransactionsRequest request) {
        institutionService.setUserId(user.getId());
        return institutionService.getTransactionsForItem(request.getItemId());
    }
}
