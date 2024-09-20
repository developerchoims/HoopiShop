package com.ms.hoopi.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UserLoginDto {
    private String id;
    private String pwd;
}
