package com.ms.hoopi.cart.service;

import com.ms.hoopi.cart.model.dto.CartRequestDto;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<String> addCart(CartRequestDto cartRequestDto);
}
