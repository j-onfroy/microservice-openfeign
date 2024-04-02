package com.company.orderservice.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItem {
    @Id
    private Integer id;

    @Column(value = "order_id")
    private Integer orderId;

    @Column(value = "product_id")
    private String productId;

    private Integer quantity;

    private Double unitPrice;

}
