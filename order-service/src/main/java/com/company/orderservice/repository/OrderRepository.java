package com.company.orderservice.repository;

import com.company.orderservice.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends R2dbcRepository<Order,Integer> {
    Flux<Order> findAllBy(Pageable pageable);
}
