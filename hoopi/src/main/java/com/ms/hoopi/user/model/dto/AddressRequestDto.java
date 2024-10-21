package com.ms.hoopi.user.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class AddressRequestDto {
    private String id;
    private String addressName;
    private String addressPhone;
    private String address;
    private String postCode;
}
