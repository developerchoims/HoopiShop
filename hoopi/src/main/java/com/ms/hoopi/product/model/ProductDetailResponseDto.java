package com.ms.hoopi.product.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ProductDetailResponseDto {
    private ProductResponseDto product;
    private String imgUrl;
    private String boardContent;
}
