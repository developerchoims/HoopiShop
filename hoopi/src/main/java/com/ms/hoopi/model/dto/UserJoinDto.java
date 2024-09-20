package com.ms.hoopi.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserJoinDto {
    private String id;
    private String pwd;
    private String name;
    private String phone;
    private String email;
    private String address;
    private LocalDate birth;
}

