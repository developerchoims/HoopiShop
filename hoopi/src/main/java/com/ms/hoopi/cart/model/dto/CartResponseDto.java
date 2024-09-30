package com.ms.hoopi.cart.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CartResponseDto {
    private String cartCode;
    private String productCode;
    private String name;
    private Long quantity;
    private Long cartAmount;
    private String imgUrl;
}
