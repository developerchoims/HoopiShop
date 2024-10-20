package com.ms.hoopi.user.model.dto;

import com.ms.hoopi.repository.AddressRepository;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private List<AddressResponseDto> addresses;
}
