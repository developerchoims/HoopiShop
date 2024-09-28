package com.ms.hoopi.admin.user.model.dto;

import com.ms.hoopi.model.entity.OrderDetailId;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UserOrderDetailSelectDto {
    private OrderDetailId orderDetailId;
    private Long quantity;
    private Long orderAmount;
    private Long totalPrice;
}
