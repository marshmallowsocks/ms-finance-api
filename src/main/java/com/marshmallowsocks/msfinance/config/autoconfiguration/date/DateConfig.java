package com.marshmallowsocks.msfinance.config.autoconfiguration.date;

import com.marshmallowsocks.msfinance.util.DateService;
import com.marshmallowsocks.msfinance.util.JodaDateService;
import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfig {

    @Bean
    DateService dateService() {
        return new JodaDateService(defaultTimeZone());
    }

    @Bean
    DateTimeZone defaultTimeZone() {
        return DateTimeZone.UTC;
    }
}
