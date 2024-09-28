package com.ms.hoopi.order.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PaymentRequestDto {
    private String paymentCode;
    private String method;
    private String bank;
    private Long paymentAmount;
}
