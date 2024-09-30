package com.ms.hoopi.order.service.serviceImpl;

import com.ms.hoopi.common.util.CommonUtil;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.Cart;
import com.ms.hoopi.model.entity.CartDetail;
import com.ms.hoopi.model.entity.Order;
import com.ms.hoopi.model.entity.OrderDetail;
import com.ms.hoopi.order.model.dto.OrderRequestDto;
import com.ms.hoopi.order.model.dto.PaymentRequestDto;
import com.ms.hoopi.order.service.OrderService;
import com.ms.hoopi.repository.*;
import jakarta.persistence.EntityNotFoundException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepoisitory orderDetailRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final PaymentRepository paymentRepository;
    private final CommonUtil commonUtil;
    private final RestTemplate restTemplate;

    @Value("${PORTONE_API_SECRET}")
    private String secret;

    @Override
    public ResponseEntity<String> addOrder(OrderRequestDto orderRequestDto) {
        try {

            PaymentRequestDto paymentRequestDto = orderRequestDto.getPaymentRequestDto();

            if (!processPayment(paymentRequestDto)) {
                return ResponseEntity.badRequest().body(Constants.ORDER_FAIL);
            }

            Cart cart = validateAndGetCart(orderRequestDto.getCartCode());
            Order order = createAndSaveOrder(cart);
            processCartDetails(order, orderRequestDto);

            return ResponseEntity.ok(Constants.ORDER_SUCCESS);
        } catch (Exception e) {
            log.error(Constants.ORDER_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.ORDER_FAIL);
        }
    }

    private Cart validateAndGetCart(String cartCode) throws EntityNotFoundException {
        return cartRepository.findByCartCode(cartCode)
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_CART));
    }

    private Order createAndSaveOrder(Cart cart) {
        Order order = Order.builder()
                .orderCode(commonUtil.createCode())
                .code(cart.getCode())
                .orderDate(LocalDateTime.now())
                .status(Order.Status.결제완료)
                .build();
        return orderRepository.save(order);
    }

    private void processCartDetails(Order order, OrderRequestDto orderRequestDto) {
        for (String productCode : orderRequestDto.getProductCode()) {
            CartDetail cartDetail = cartDetailRepository.findByCartCodeAndProductCode(orderRequestDto.getCartCode(), productCode)
                    .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_CART_PRODUCT));
            createAndSaveOrderDetail(order, cartDetail);
            cartDetailRepository.delete(cartDetail);
        }
        changeCartStatusToComplete(orderRequestDto.getCartCode());
    }

    private void createAndSaveOrderDetail(Order order, CartDetail cartDetail) {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderCode(order)
                .productCode(cartDetail.getProductCode())
                .quantity(cartDetail.getQuantity())
                .orderAmount(cartDetail.getCartAmount())
                .totalPrice(cartDetail.getCartAmount())
                .build();
        orderDetailRepository.save(orderDetail);
    }

    private boolean processPayment(PaymentRequestDto paymentRequestDto) {
        String url = "https://api.portone.io/payments/" + paymentRequestDto.getPaymentCode();
        log.info("Payment URL: {}", url);
        HttpResponse<String> paymentResponse = Unirest.get(url)
                .header("Authorization", "PortOne " + secret)
                .header("Content-Type", "application/json")
                .asString();
        log.info("Payment Response: {}", paymentResponse.getBody());
        if(!paymentResponse.isSuccess()){
            log.error("Failed to process payment: Status={}, Body={}",
                    paymentResponse.getStatus(), paymentResponse.getBody());
        }
        return paymentResponse.isSuccess();
    }

    private void changeCartStatusToComplete(String cartCode) {
        cartRepository.findByCartCode(cartCode).ifPresent(cart -> {
            Cart changeCart = Cart.builder()
                                    .cartCode(cart.getCartCode())
                                    .code(cart.getCode())
                                    .status("Y")
                                    .build();
            cartRepository.save(changeCart);
        });
    }
}
