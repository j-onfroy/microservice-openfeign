package com.company.orderservice.controller;

import com.company.orderservice.entity.Order;
import com.company.orderservice.payload.OrderAddDto;
import com.company.orderservice.payload.OrderDto;
import com.company.orderservice.service.OrderService;
import com.company.orderservice.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(OrderController.BASE_PATH)
@RequiredArgsConstructor
public class OrderController {
    static final String BASE_PATH = AppConstant.BASE_PATH + "/order";
    private final OrderService orderService;

    @GetMapping
    public Flux<OrderDto> allOrders(@RequestParam(defaultValue = "${app.default.page}") int page,
                                    @RequestParam(defaultValue = "${app.default.size}") int size) {
        return orderService.list(page, size);
    }

    @GetMapping("/my")
    public Flux<OrderDto> allMyOrders(@RequestParam(defaultValue = "${app.default.page}") int page,
                                      @RequestParam(defaultValue = "${app.default.size}") int size) {
        return orderService.myOrders(page, size);
    }

    @GetMapping("/{id}")
    public Mono<OrderDto> one(@PathVariable Integer id) {
        return orderService.byId(id);
    }

    @GetMapping("/test-pagination")
    public Mono<String> test(@RequestParam(defaultValue = "${app.default.page}") int page,
                             @RequestParam(defaultValue = "${app.default.size}") int size
    ) {
        return Mono.just("Page: " + page + ", Size: " + size);
    }

    @PostMapping
    public Mono<OrderDto> create(@RequestBody List<OrderAddDto> orderAddDtos){
        return orderService.add(orderAddDtos);
    }
}
