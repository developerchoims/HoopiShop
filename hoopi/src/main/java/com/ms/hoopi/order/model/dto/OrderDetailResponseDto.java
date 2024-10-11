package com.ms.hoopi.order.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class OrderDetailResponseDto{
    private Long quantity;
    private Long orderAmount;
    private Long totalPrice;
}
