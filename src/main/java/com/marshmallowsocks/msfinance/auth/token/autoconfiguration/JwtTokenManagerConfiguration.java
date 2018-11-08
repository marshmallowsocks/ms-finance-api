package com.marshmallowsocks.msfinance.auth.token.autoconfiguration;

import com.marshmallowsocks.msfinance.auth.token.manager.JwtTokenManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class JwtTokenManagerConfiguration {

    @Bean
    public JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }
}
