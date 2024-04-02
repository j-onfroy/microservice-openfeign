package com.company.appgatewayapi.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(predicateSpec -> predicateSpec.path("/api/v1/order/**")
                        .uri("lb://order-service"))
                .route(predicateSpec -> predicateSpec.path("/api/v1/product/**")
                        .uri("lb://product-service"))
                .build();
    }
}
