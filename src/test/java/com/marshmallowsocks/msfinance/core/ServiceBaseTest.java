package com.marshmallowsocks.msfinance.core;


import graphql.schema.DataFetchingEnvironment;
import io.leangen.graphql.execution.GlobalEnvironment;
import io.leangen.graphql.execution.ResolutionEnvironment;
import io.leangen.graphql.metadata.strategy.value.ValueMapper;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ServiceBaseTest {

    @Mock
    protected ValueMapper valueMapper;

    @Mock
    protected GlobalEnvironment globalEnvironment;

    @Mock
    protected DataFetchingEnvironment dataFetchingEnvironment;

    protected ResolutionEnvironment environment;

    protected void init() {
        when(dataFetchingEnvironment.getContext()).thenReturn("1234");
        environment = new ResolutionEnvironment(dataFetchingEnvironment, valueMapper, globalEnvironment);
    }

    protected ResponseBody error() {
        return new ResponseBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public long contentLength() {
                return 0;
            }

            @Override
            public BufferedSource source() {
                return null;
            }
        };
    }
}
