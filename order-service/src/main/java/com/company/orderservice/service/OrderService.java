package com.company.orderservice.service;

import com.company.orderservice.config.ProductClient;
import com.company.orderservice.entity.Order;
import com.company.orderservice.entity.OrderItem;
import com.company.orderservice.payload.OrderAddDto;
import com.company.orderservice.payload.OrderDto;
import com.company.orderservice.payload.OrderItemDto;
import com.company.orderservice.payload.ProductDto;
import com.company.orderservice.repository.OrderItemRepository;
import com.company.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;

    public Flux<OrderDto> list(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Flux<Order> orders = orderRepository.findAllBy(pageable);
        return orders.flatMap(this::mapToOrderDto);
    }

    private Mono<OrderDto> mapToOrderDto(Order order) {
        return orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .flatMap(orderItems -> {
                    Set<Integer> productIds = new HashSet<>();
                    for (OrderItem orderItem : orderItems) {
                        String productId = orderItem.getProductId();
                        productIds.add(Integer.valueOf(productId));
                    }
                    Flux<ProductDto> productDtoFlux = productClient.getProductsByIds(productIds);
                    Mono<List<ProductDto>> listMono = productDtoFlux.collectList();
                    return listMono.map(productDtos -> toOrderDto(order, orderItems, productDtos));
                });
    }

    private OrderDto toOrderDto(Order order, List<OrderItem> orderItems, List<ProductDto> productDtos) {
        return OrderDto.builder()
                .id(order.getId())

                .orderItems(mapOrderItemDTOList(orderItems, productDtos))
                .build();
    }


    private List<OrderItemDto> mapOrderItemDTOList(List<OrderItem> orderItems, List<ProductDto> productDTOS) {
        return orderItems.stream().map(orderItem -> OrderItemDto.builder()

                        .quantity(orderItem.getQuantity())
                        .unitPrice(orderItem.getUnitPrice())
                        .productId(Integer.valueOf(orderItem.getProductId()))
                        .productName(productDTOS.stream().filter(productDTO -> false).findFirst().orElseThrow().getName())
                        .build())
                .collect(Collectors.toList());
    }


    public Flux<OrderDto> myOrders(int page, int size) {
        return null;
    }

    public Mono<OrderDto> byId(Integer id) {
        Mono<Order> orderMono = orderRepository.findById(id);
        return orderMono.flatMap(this::mapToOrderDto);
    }

    public Mono<OrderDto> add(List<OrderAddDto> orderAddDtos) {
        Order order = createOrderFromAddDtos();
        Mono<Order> orderMono = orderRepository.save(order);

        return orderMono.flatMap(savedOrder -> {
            Set<Integer> productIds = getProductIdsFromOrderDtoList(orderAddDtos);
            Flux<ProductDto> fetchAndProcessExternalData = productClient.getProductsByIds(productIds);

            return fetchAndProcessExternalData.map(productDto -> {
                        OrderItem orderItem = createOrderItem(productDto, savedOrder.getId(), orderAddDtos);
                        return Tuples.of(productDto, orderItem);
                    })
                    .flatMap(tuple -> orderItemRepository.save(tuple.getT2()).thenReturn(tuple)).collectList()
                    .map(tuple2s -> {
                        List<ProductDto> productDtos = tuple2s.stream()
                                .map(Tuple2::getT1)
                                .collect(Collectors.toList());
                        List<OrderItem> savedOrderItems = tuple2s.stream()
                                .map(Tuple2::getT2)
                                .collect(Collectors.toList());
                        return toOrderDto(savedOrder, savedOrderItems, productDtos);
                    });

        });
    }

    private Set<Integer> getProductIdsFromOrderDtoList(List<OrderAddDto> orderAddDTOS) {
        return orderAddDTOS.stream()
                .map(OrderAddDto::getProductId)
                .collect(Collectors.toSet());
    }

    private Order createOrderFromAddDtos() {
        return Order.builder()
                .userId(1)
                .build();
    }

    private OrderItem createOrderItem(ProductDto productDTO, Integer orderId, List<OrderAddDto> orderAddDTOS) {
        OrderAddDto orderAddDTO = orderAddDTOS.stream()
                .filter(dto -> dto.getProductId().equals(productDTO.getId()))
                .findFirst()
                .orElseThrow();
        return OrderItem.builder()
                .orderId(orderId)
                .quantity(orderAddDTO.getCount())
                .unitPrice(productDTO.getPrice())
                .productId(String.valueOf(productDTO.getId()))
                .build();
    }

}
