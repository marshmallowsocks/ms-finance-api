package com.marshmallowsocks.msfinance.core.accesstoken;

import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.user.model.User;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@RestController
@RequestMapping("/tokens")
public class AccessTokenController {

    private AccessTokenService accessTokenService;

    @Autowired
    public AccessTokenController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostMapping("/exchange")
    public ItemPublicTokenExchangeResponse exchange(Authentication authentication, @RequestParam(name = "publicToken") String publicToken) {
        User user = (User) authentication.getPrincipal();
        accessTokenService.setUserId(user.getId());

        return accessTokenService.exchangePublicToken(publicToken);
    }

    @GetMapping("/all")
    public List<AccessToken> getAllTokens(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        accessTokenService.setUserId(user.getId());

        return accessTokenService.fetchAll();
    }
}
