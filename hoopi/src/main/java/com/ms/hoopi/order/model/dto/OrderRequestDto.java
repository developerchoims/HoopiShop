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
    private String address;
    private List<String> productCode;
    String storeId;
    private PaymentRequestDto paymentRequestDto;
}
