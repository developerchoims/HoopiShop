package com.ms.hoopi.admin.user.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UserSelectResponseDto {
    private String code;
    private String userId;
    private String userName;
    private String email;
    private String phone;
}
