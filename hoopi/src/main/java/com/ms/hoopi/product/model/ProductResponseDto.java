package com.ms.hoopi.product.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ProductResponseDto {
    private String productCode;
    private String name;
    private Long price;
    private LocalDateTime createdAt;
}
