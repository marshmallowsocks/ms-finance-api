package com.marshmallowsocks.msfinance.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

public class NoRedirectStrategyTest {

    private NoRedirectStrategy noRedirectStrategy;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setup() {
        noRedirectStrategy = new NoRedirectStrategy();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void sendRedirect_shouldNotRedirect() throws IOException {
        noRedirectStrategy.sendRedirect(request, response, "");
        Assert.assertNotEquals(HttpStatus.Series.valueOf(response.getStatus()),HttpStatus.Series.REDIRECTION);
    }
}
