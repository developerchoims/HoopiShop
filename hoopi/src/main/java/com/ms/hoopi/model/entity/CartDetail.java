package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cart_detail")
public class CartDetail {
    @EmbeddedId
    private CartDetailId id;

    @JsonBackReference
    @MapsId("productCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_code", nullable = false)
    private Product productCode;

    @JsonBackReference
    @MapsId("cartCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_code", nullable = false)
    private Cart cartCode;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "cart_amount", nullable = false)
    private Long cartAmount;

    @Builder
    public CartDetail(CartDetailId id, Product productCode, Cart cartCode, Long quantity, Long cartAmount) {
        this.id = id;
        this.productCode = productCode;
        this.cartCode = cartCode;
        this.quantity = quantity;
        this.cartAmount = cartAmount;
    }

}