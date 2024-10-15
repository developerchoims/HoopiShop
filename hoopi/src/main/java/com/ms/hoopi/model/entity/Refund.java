package com.ms.hoopi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "refund")
public class Refund {
    @Id
    @Size(max = 255)
    @Column(name = "refund_code", nullable = false)
    private String refundCode;

    @NotNull
    @Column(name = "refund_date", nullable = false)
    private LocalDateTime refundDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_code", nullable = false)
    private Payment paymentCode;

    @Builder
    public Refund(String refundCode, LocalDateTime refundDate, Payment paymentCode) {
        this.refundCode = refundCode;
        this.refundDate = refundDate;
        this.paymentCode = paymentCode;
    }
}