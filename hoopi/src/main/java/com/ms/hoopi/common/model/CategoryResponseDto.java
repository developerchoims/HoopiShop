package com.ms.hoopi.common.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class CategoryResponseDto {
    private Integer id;
    private Integer depth;
    private String name;
    private Integer superId;
    private String categoryId;
}
