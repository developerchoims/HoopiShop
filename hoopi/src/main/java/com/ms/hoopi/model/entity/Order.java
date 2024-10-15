package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @Column(name = "order_code", nullable = false)
    private String orderCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
    private User code;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.결제완료;

    @Size(max = 255)
    @NotNull
    @Column(name = "address_code", nullable = false)
    private String addressCode;

    public enum Status {
        결제완료,
        배송중,
        배송완료,
        주문취소
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "orderCode")
    private Set<Delivery> deliveries = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "orderCode")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "orderCode")
    private Set<Payment> payments = new LinkedHashSet<>();


    @Builder
    public Order(String orderCode, User code, LocalDateTime orderDate, Status status, String addressCode) {
        this.orderCode = orderCode;
        this.code = code;
        this.orderDate = orderDate;
        this.status = status;
        this.addressCode = addressCode;
    }
}