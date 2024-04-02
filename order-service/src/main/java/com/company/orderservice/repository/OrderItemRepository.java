package com.company.orderservice.repository;

import com.company.orderservice.entity.Order;
import com.company.orderservice.entity.OrderItem;
import com.company.orderservice.payload.OrderDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Integer> {
    Flux<OrderItem> findAllBy(Pageable pageable);

    Flux<OrderItem> findAllByOrderId(Integer id);
}
