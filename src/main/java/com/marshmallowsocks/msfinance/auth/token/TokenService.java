package com.marshmallowsocks.msfinance.auth.token;

import java.util.Map;

public interface TokenService {

    JwtToken permanent(Map<String, String> attributes);
    JwtToken expiring(Map<String, String> attributes);

    /**
     * Checks the validity of the given credentials.
     *
     * @param token
     * @return attributes if verified
     */
    Map<String, String> untrusted(String token);

    /**
     * Checks the validity of the given credentials.
     *
     * @param token
     * @return attributes if verified
     */
    Map<String, String> verify(String token);

    void invalidate(JwtToken token);
}
