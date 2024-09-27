package com.ms.hoopi.cart.controller;

import com.ms.hoopi.cart.model.dto.CartPutRequestDto;
import com.ms.hoopi.cart.model.dto.CartRequestDto;
import com.ms.hoopi.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/cart")
    public ResponseEntity<?> getCart(@RequestParam String id) {
        return cartService.getCart(id);
    }

    @PutMapping("/cart")
    public ResponseEntity<String> updateCart(@RequestBody CartPutRequestDto cartPutRequestDto) {
        return cartService.updateCart(cartPutRequestDto);
    }

    @DeleteMapping("/cart-part")
    public ResponseEntity<String> deleteCartPart(@RequestParam String cartCode, @RequestParam List<String> productCodes) {
        return cartService.deleteCartPart(cartCode, productCodes);
    }

    @DeleteMapping("/cart-all")
    public ResponseEntity<String> deleteCartAll(@RequestParam String cartCode) {
        return cartService.deleteCartAll(cartCode);
    }
}
