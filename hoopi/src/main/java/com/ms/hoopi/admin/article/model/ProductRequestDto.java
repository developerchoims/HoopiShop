package com.ms.hoopi.admin.article.model;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ProductRequestDto {
    private String name;
    private Long price;
    private Long stock;
}
