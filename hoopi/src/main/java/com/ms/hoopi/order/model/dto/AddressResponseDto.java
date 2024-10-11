package com.ms.hoopi.order.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class AddressResponseDto{
    private String addressCode;
    private String addressName;
    private String addressPhone;
    private String address;
}