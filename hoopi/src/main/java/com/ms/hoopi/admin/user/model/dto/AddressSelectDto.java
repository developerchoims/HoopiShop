package com.ms.hoopi.admin.user.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class AddressSelectDto {
    private String addressCode;
    private String address;
    private String main;
}
