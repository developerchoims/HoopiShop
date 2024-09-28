package com.ms.hoopi.order.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderRequestDto {
    private String cartCode;
    private List<String> productCode;
    private PaymentRequestDto paymentRequestDto;
}
