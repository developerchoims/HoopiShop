package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "payment_code", nullable = false)
    private String paymentCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_code", nullable = false)
    private Order orderCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
    private User code;

    @Column(name = "method", length = 100)
    private String method;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "bank", length = 150)
    private String bank;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.결제완료;

    public enum Status{
        결제완료,
        결제취소
    }

    @Builder
    public Payment (String paymentCode, Order orderCode, User code, String method, String bank, LocalDateTime paymentDate, Long paymentAmount, Status status) {
        this.paymentCode = paymentCode;
        this.orderCode = orderCode;
        this.code = code;
        this.method = method;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.status = status;
    }

}