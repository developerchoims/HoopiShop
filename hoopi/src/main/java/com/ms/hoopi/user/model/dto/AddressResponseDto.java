package com.ms.hoopi.user.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class AddressResponseDto {
    private String addressCode;
    private String addressName;
    private String address;
    private String addressPhone;
    private String postCode;
    private String main;
}
