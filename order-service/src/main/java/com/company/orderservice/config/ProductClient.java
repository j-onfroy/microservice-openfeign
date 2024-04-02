package com.company.orderservice.config;

import com.company.orderservice.payload.ProductDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;

import java.util.Set;

@HttpExchange
public interface ProductClient {
    @PostExchange("//api/v1/product/product/by-ids")
    Flux<ProductDto> getProductsByIds(@RequestBody Set<Integer> ids);
}
