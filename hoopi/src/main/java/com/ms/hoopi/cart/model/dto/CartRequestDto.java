package com.ms.hoopi.cart.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CartRequestDto {
    private String id;
    private String productCode;
    private Long quantity;
    private Long cartAmount;
}
