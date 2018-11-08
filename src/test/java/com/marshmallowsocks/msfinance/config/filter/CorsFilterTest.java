package com.marshmallowsocks.msfinance.config.filter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CorsFilterTest {

    @Mock
    private FilterChain filter;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private CorsFilter corsFilter;

    @Before
    public void setup() throws IOException, ServletException {
//        doCallRealMethod().when(response).setHeader(anyString(), anyString());
//        when(response.getHeader(anyString())).thenCallRealMethod();
//        doCallRealMethod().when(response).setStatus(anyInt());
//        when(response.getStatus()).thenCallRealMethod();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        corsFilter = new CorsFilter();

        doNothing().when(filter).doFilter(request, response);
    }

    @Test
    public void doFilter_nonOptions_correctResponseHeaders() throws IOException, ServletException {
        // arrange
        request.setMethod(HttpMethod.GET.name());

        // act
        corsFilter.doFilter(request, response, filter);

        // assert
        Assert.assertEquals(response.getHeader("Access-Control-Allow-Origin"), "http://localhost:3000");
        Assert.assertEquals(response.getHeader("Access-Control-Allow-Methods"), "GET,POST,DELETE,PUT,OPTIONS");
        Assert.assertEquals(response.getHeader("Access-Control-Allow-Headers"), "content-type,authorization");
        Assert.assertEquals(response.getHeader("Access-Control-Allow-Credentials"), "true");
        Assert.assertEquals(response.getHeader("Access-Control-Max-Age"), "180");
    }

    @Test
    public void doFilter_options_returnOK() throws IOException, ServletException {
        // arrange
        request.setMethod(HttpMethod.OPTIONS.name());

        // act
        corsFilter.doFilter(request, response, filter);

        // assert
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
    }
}
