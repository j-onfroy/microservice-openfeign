package com.company.orderservice.payload;

import lombok.Getter;

@Getter
public class OrderAddDto {
    private Integer productId;
    private Integer count;
}
