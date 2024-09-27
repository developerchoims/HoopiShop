package com.ms.hoopi.cart.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class CartPutRequestDto {
    private String CartCode;
    private String productCode;
    private Long quantity;
    private Long cartAmount;
}
