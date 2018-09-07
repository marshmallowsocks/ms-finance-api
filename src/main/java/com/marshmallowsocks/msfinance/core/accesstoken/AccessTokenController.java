package com.marshmallowsocks.msfinance.core.accesstoken;

import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.user.model.User;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/tokens", consumes = "application/json")
public class AccessTokenController {

    private AccessTokenService accessTokenService;

    @Autowired
    public AccessTokenController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostMapping("/exchange")
    public ItemPublicTokenExchangeResponse exchange(@AuthenticationPrincipal final User user, @RequestParam(name = "publicToken") String publicToken) {
        accessTokenService.setUserId(user.getId());
        return accessTokenService.exchangePublicToken(publicToken);
    }

    @GetMapping("/all")
    public List<AccessToken> getAllTokens(@AuthenticationPrincipal final User user) {
        accessTokenService.setUserId(user.getId());
        return accessTokenService.fetchAll();
    }
}
