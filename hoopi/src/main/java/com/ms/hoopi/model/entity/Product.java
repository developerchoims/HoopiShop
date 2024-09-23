package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Long stock;

    @JsonManagedReference
    @OneToMany(mappedBy = "productCode")
    private Set<Article> articles = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "productCode")
    private Set<CartDetail> cartDetails = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "productCode")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "productCode")
    private Set<ProductImg> productImgs = new LinkedHashSet<>();

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Product(String productCode, String name, Long price, Long stock) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

}