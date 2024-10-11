package com.ms.hoopi.order.service;

import com.ms.hoopi.order.model.dto.OrderRequestDto;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<String> addOrder(OrderRequestDto orderRequestDto);

    ResponseEntity<?> getOrder(String id, int page, int size);
}
