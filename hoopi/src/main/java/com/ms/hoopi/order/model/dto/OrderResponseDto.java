package com.ms.hoopi.order.model.dto;

import com.ms.hoopi.model.entity.Address;
import com.ms.hoopi.model.entity.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class OrderResponseDto {

    private String orderCode;
    private LocalDateTime orderDate;
    private Order.Status orderStatus;
    private AddressResponseDto address;
    private List<OrderDetailResponseDto> orderDetails;
}


