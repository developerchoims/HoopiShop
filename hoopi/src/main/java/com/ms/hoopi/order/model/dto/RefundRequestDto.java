package com.ms.hoopi.order.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class RefundRequestDto {
    String orderCode;
    String reason;
}
