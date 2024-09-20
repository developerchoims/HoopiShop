package com.ms.hoopi.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UserLoginResponseDto {
    private String id;
    private String role;
    private String msg;
}
