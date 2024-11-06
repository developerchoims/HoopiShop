package com.ms.hoopi.admin.order.service.serviceImpl;

import com.ms.hoopi.admin.order.model.OrderResponseDto;
import com.ms.hoopi.admin.order.service.AdminOrderService;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.Order;
import com.ms.hoopi.repository.OrderRepository;
import com.ms.hoopi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> getOrder() {
        try{
            List<Order> orders = orderRepository.findAll();
            List<OrderResponseDto> orderDtos = orders.stream().map(o -> {
                return OrderResponseDto.builder()
                        .id(userRepository.findUserIdByUser(o.getCode()))
                        .orderDate(o.getOrderDate())
                        .orderCode(o.getOrderCode())
                        .status(o.getStatus())
                        .build();
            }).toList();
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<OrderResponseDto> orderList = new PageImpl<>(orderDtos, pageRequest, orderDtos.size());
            return ResponseEntity.ok(orderList);

        } catch (Exception e){
            log.error(Constants.NONE_ORDER, e);
            return ResponseEntity.badRequest().body(Constants.NONE_ORDER);
        }
    }
}
