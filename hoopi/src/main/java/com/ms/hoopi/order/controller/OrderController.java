package com.ms.hoopi.order.controller;

import com.ms.hoopi.order.model.dto.OrderRequestDto;
import com.ms.hoopi.order.model.dto.RefundRequestDto;
import com.ms.hoopi.order.service.OrderService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("hoopi")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> addOrder(@RequestBody OrderRequestDto orderRequestDto){
        return orderService.addOrder(orderRequestDto);
    }

    @GetMapping("/order")
    public ResponseEntity<?> getOrder(@RequestParam String id
                                    , @RequestParam(defaultValue = "0")int page
                                    , @RequestParam(defaultValue = "10") int size){
        return orderService.getOrder(id, page, size);
    }

    @PutMapping("/order")
    public ResponseEntity<String> requestRefund(@RequestBody RefundRequestDto refundRequestDto){
        return orderService.requestRefund(refundRequestDto);
    }
}
