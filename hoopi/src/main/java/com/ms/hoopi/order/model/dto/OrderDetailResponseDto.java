package com.ms.hoopi.order.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class OrderDetailResponseDto{
    private String productName;
    private String productImg;
    private Long quantity;
    private Long orderAmount;
    private Long totalPrice;
}
