package com.ms.hoopi.admin.user.model.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UserDetailSelectResponseDto {
    private String code;
    private String id;
    private String name;
    private String phone;
    private String email;
    private String quitYn;
    private LocalDateTime joinDate;
    private LocalDateTime quitDate;
    private LocalDate birth;
    private List<AddressSelectDto> addressDto;


}
