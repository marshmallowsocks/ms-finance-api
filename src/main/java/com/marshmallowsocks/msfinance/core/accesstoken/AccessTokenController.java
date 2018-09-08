package com.marshmallowsocks.msfinance.core.accesstoken;

import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.user.model.User;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/tokens", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccessTokenController {

    private AccessTokenService accessTokenService;

    @Autowired
    public AccessTokenController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostMapping("/exchange")
    public ItemPublicTokenExchangeResponse exchange(@AuthenticationPrincipal final User user, @RequestBody AccessTokenRequest request) {
        accessTokenService.setUserId(user.getId());
        return accessTokenService.exchangePublicToken(request.getPublicToken());
    }

    @GetMapping("/all")
    public List<AccessToken> getAllTokens(@AuthenticationPrincipal final User user) {
        accessTokenService.setUserId(user.getId());
        return accessTokenService.fetchAll();
    }
}
