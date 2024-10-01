package com.ms.hoopi.order.service.serviceImpl;

import com.ms.hoopi.common.util.CommonUtil;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.*;
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
import java.util.HashMap;
import java.util.Map;

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
            // 사전 등록
            preRegistPayment(orderRequestDto);

            // 결제 확인
            PaymentRequestDto paymentRequestDto = orderRequestDto.getPaymentRequestDto();
            processPayment(paymentRequestDto);

            // order, orderDetail 정보 저장, cart status 변경, payment 정보 저장
            Cart cart = validateAndGetCart(orderRequestDto.getCartCode());
            Order order = createAndSaveOrder(cart);
            processCartDetails(order, orderRequestDto);

            return ResponseEntity.ok(Constants.ORDER_SUCCESS);
        } catch (Exception e) {
            log.error(Constants.ORDER_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.ORDER_FAIL);
        }
    }
    private void preRegistPayment(OrderRequestDto orderRequestDto){
        String paymentId = orderRequestDto.getPaymentRequestDto().getPaymentCode();
        String storeId = orderRequestDto.getStoreId();
        Long totalAmount = orderRequestDto.getPaymentRequestDto().getPaymentAmount();
        Long taxFreeAmount = 0L;
        String currency = "KRW";
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("totalAmount", totalAmount.toString());
        map.put("taxFreeAmount", taxFreeAmount.toString());
        map.put("currency", currency);

        HttpResponse<String> response = Unirest.post("https://api.portone.io/payments/"+paymentId+"/pre-register")
                .header("Content-Type", "application/json")
                .body(map)
                .asString();
        log.error("response: {}", response);
        returnResponse(response);
    }

    private void processPayment(PaymentRequestDto paymentRequestDto) {
        String url = "https://api.portone.io/payments/" + paymentRequestDto.getPaymentCode();
        log.info("Payment URL: {}", url);
        HttpResponse<String> paymentResponse = Unirest.post(url)
                .header("Authorization", "PortOne " + secret)
                .header("Content-Type", "application/json")
                .asString();
        log.info("secret: {}", secret);
        log.info("Payment Response: {}", paymentResponse.getBody());
        if(!paymentResponse.isSuccess()){
            log.error("Failed to process payment: Status={}, Body={}",
                    paymentResponse.getStatus(), paymentResponse.getBody());
        }

        returnResponse(paymentResponse);
    }


    private void returnResponse(HttpResponse<String> response) {
        switch (response.getStatus()) {
            case 200 -> {
                break;
            }
            case 400 -> ResponseEntity.badRequest().body(Constants.ORDER_INVALID_REQUEST);
            case 401 -> ResponseEntity.badRequest().body(Constants.ORDER_UNAUTHORIZED);
            case 403 -> ResponseEntity.badRequest().body(Constants.ORDER_FORBIDDEN);
            case 404 -> ResponseEntity.badRequest().body(Constants.ORDER_PAYMENT_NOT_FOUND);
            case 502 -> ResponseEntity.badRequest().body(Constants.ORDER_PG);
            default -> ResponseEntity.badRequest().body(Constants.ORDER_FAIL);
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
        addPayment(orderRequestDto.getPaymentRequestDto(), order);
        changeCartStatusToComplete(orderRequestDto.getCartCode());
    }

    private void createAndSaveOrderDetail(Order order, CartDetail cartDetail) {
        OrderDetailId orderDetailId = OrderDetailId.builder()
                .orderCode(order.getOrderCode())
                .productCode(cartDetail.getProductCode().getProductCode())
                .build();
        OrderDetail orderDetail = OrderDetail.builder()
                .id(orderDetailId)
                .orderCode(order)
                .productCode(cartDetail.getProductCode())
                .quantity(cartDetail.getQuantity())
                .orderAmount(cartDetail.getCartAmount())
                .totalPrice(cartDetail.getCartAmount())
                .build();
        orderDetailRepository.save(orderDetail);
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

    private void addPayment(PaymentRequestDto paymentRequestDto, Order order) {
        Payment payment = Payment.builder()
                .paymentCode(paymentRequestDto.getPaymentCode())
                .orderCode(order)
                .code(order.getCode())
                .paymentAmount(paymentRequestDto.getPaymentAmount())
                .bank(paymentRequestDto.getBank())
                .method(paymentRequestDto.getMethod())
                .status(Payment.Status.결제완료)
                .build();
        paymentRepository.save(payment);
    }
}
