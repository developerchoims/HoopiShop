package com.ms.hoopi.admin.order.model;

import com.ms.hoopi.model.entity.Order;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class OrderResponseDto {
    private String id;
    private LocalDateTime orderDate;
    private String orderCode;
    private Order.Status status;
}
