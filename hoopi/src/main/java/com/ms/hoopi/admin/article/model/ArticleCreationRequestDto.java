package com.ms.hoopi.admin.article.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ArticleCreationRequestDto {
    private ProductRequestDto product;
    private ArticleRequestDto article;
}
