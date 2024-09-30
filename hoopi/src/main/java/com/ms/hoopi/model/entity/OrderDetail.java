package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId id;

    @JsonBackReference
    @MapsId("productCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_code", nullable = false)
    private Product productCode;

    @JsonBackReference
    @MapsId("orderCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_code", nullable = false)
    private Order orderCode;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "order_amount", nullable = false)
    private Long orderAmount;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Builder
    public OrderDetail (OrderDetailId id, Product productCode, Order orderCode, Long quantity, Long orderAmount, Long totalPrice) {
        this.productCode = productCode;
        this.orderCode = orderCode;
        this.quantity = quantity;
        this.orderAmount = orderAmount;
        this.totalPrice = totalPrice;
        this.id = id;
    }

}