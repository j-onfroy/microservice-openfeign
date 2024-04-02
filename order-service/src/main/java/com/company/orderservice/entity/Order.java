package com.company.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "order")
public class Order {
    @Id
    private Integer id;
    @Column(value = "user_id")
    private Integer userId;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
