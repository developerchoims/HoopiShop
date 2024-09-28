package com.ms.hoopi.order.controller;

import com.ms.hoopi.order.model.dto.OrderRequestDto;
import com.ms.hoopi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("hoopi")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> addOrderOne(@RequestBody OrderRequestDto orderRequestDto){
        return orderService.addOrderOne(orderRequestDto);
    }
}
