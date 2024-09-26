package com.ms.hoopi.cart.controller;

import com.ms.hoopi.cart.model.dto.CartRequestDto;
import com.ms.hoopi.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("hoopi")
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<String> addCart(@RequestBody CartRequestDto cartRequestDto) {
        return cartService.addCart(cartRequestDto);
    }
}
