package com.company.appnotificationservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final LoadBalancedExchangeFilterFunction filterFunction;

    @Bean
    public AuthClient productionClient() {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder().exchangeAdapter(WebClientAdapter.create(
                        WebClient.builder().baseUrl("lb://auth-service")
                                .filter(filterFunction)
                                .build()
                ))
                .build();
        return httpServiceProxyFactory.createClient(AuthClient.class);
    }
}
