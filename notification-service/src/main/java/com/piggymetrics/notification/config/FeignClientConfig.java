package com.piggymetrics.notification.config;

import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Bean
    public OAuthRequestInterceptor requestInterceptor() {
        return new OAuthRequestInterceptor();
    }
    
}
