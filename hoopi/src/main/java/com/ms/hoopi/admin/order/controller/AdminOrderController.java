package com.ms.hoopi.admin.order.controller;

import com.ms.hoopi.admin.order.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hoopi/admin")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/order")
    public ResponseEntity<?> getOrder() {
        return adminOrderService.getOrder();
    }
}
