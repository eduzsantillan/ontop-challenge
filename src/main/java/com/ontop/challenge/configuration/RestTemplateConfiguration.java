package com.ontop.challenge.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    private static final String MOCK_SERVICE_HOST = "http://mockoon.tools.getontop.com:3000/";

    @Bean("MockServiceRest")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(MOCK_SERVICE_HOST)
                .build();
    }
}
