package com.ms.hoopi.cart.service;

import com.ms.hoopi.cart.model.dto.CartPutRequestDto;
import com.ms.hoopi.cart.model.dto.CartRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    ResponseEntity<String> addCart(CartRequestDto cartRequestDto);

    ResponseEntity<?> getCart(String id);

    ResponseEntity<String> updateCart(CartPutRequestDto cartPutRequestDto);

    ResponseEntity<String> deleteCartPart(String cartCode, List<String> productCodes);

    ResponseEntity<String> deleteCartAll(String cartCode);
}
