package com.ms.hoopi.order.service.serviceImpl;

import com.ms.hoopi.common.service.FileUploadService;
import com.ms.hoopi.common.util.CommonUtil;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.*;
import com.ms.hoopi.order.model.dto.*;
import com.ms.hoopi.order.service.OrderService;
import com.ms.hoopi.repository.*;
import jakarta.persistence.EntityNotFoundException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final FileUploadService fileUploadService;
    private final RefundRepository refundRepository;

    @Value("${PORTONE_API_SECRET}")
    private String secret;

    @Override
    public ResponseEntity<String> addOrder(OrderRequestDto orderRequestDto) {
        try {
            // 사전 등록 및 결제 실패 로직
            preRegistPayment(orderRequestDto);

            // 결제 확인 및 결제 실패 로직
            PaymentRequestDto paymentRequestDto = orderRequestDto.getPaymentRequestDto();
            processPayment(paymentRequestDto);

            // order, orderDetail 정보 저장, cart status 변경, payment 정보 저장
            Cart cart = validateAndGetCart(orderRequestDto.getCartCode());
            Order order = createAndSaveOrder(cart, orderRequestDto.getAddress());
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
                .header("Authorization", "PortOne " + secret)
                .header("Content-Type", "application/json")
                .body(map)
                .asString();
        if(!response.isSuccess()){
            cancelPayment(orderRequestDto.getPaymentRequestDto(), response);
        }
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
            cancelPayment(paymentRequestDto, paymentResponse);
        }

    }

    private void cancelPayment(PaymentRequestDto paymentRequestDto, HttpResponse<String> response){
        String url = "https://api.portone.io/payments/" + paymentRequestDto.getPaymentCode() + "/cancel";
        log.info("Payment URL: {}", url);
        String reason = "";
        switch (response.getStatus()) {
            case 200 -> {
                break;
            }
            case 400 -> reason = Constants.ORDER_INVALID_REQUEST;
            case 401 -> reason = Constants.ORDER_UNAUTHORIZED;
            case 403 -> reason = Constants.ORDER_FORBIDDEN;
            case 404 -> reason = Constants.ORDER_PAYMENT_NOT_FOUND;
            case 502 -> reason = Constants.ORDER_PG;
            default -> reason = Constants.ORDER_FAIL;
        }
        HttpResponse<String> cancelResponse = Unirest.post("https://api.portone.io/payments/"+ paymentRequestDto.getPaymentCode() +"/cancel")
                .header("Authorization", "PortOne " + secret)
                .header("Content-Type", "application/json")
                .body("{\"reason\":\"" + reason + "\"}")
                .asString();
        if(!cancelResponse.isSuccess()){
            log.error("Failed to process payment: Status={}, Body={}",
                    cancelResponse.getStatus(), cancelResponse.getBody());
        }
        returnResponse(cancelResponse);
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

    private Order createAndSaveOrder(Cart cart, String addressCode) {
        Order order = Order.builder()
                .orderCode(commonUtil.createCode())
                .code(cart.getCode())
                .orderDate(LocalDateTime.now())
                .status(Order.Status.결제완료)
                .addressCode(addressCode)
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
                .paymentDate(order.getOrderDate())
                .status(Payment.Status.결제완료)
                .build();
        paymentRepository.save(payment);
    }


    // order 정보 불러오기
    @Override
    public ResponseEntity<?> getOrder(String id, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_USER));
            Page<Order> orderEntity = orderRepository.findAllByUserCode(user, pageable);

            Page<OrderResponseDto> orderResponsePage = orderEntity.map(order -> {
                Address addressEntity = addressRepository.findByAddressCode(order.getAddressCode());
                AddressResponseDto address = AddressResponseDto.builder()
                        .addressCode(addressEntity.getAddressCode())
                        .addressName(addressEntity.getAddressName())
                        .addressPhone(addressEntity.getAddressPhone())
                        .address(addressEntity.getAddress())
                        .build();

                return OrderResponseDto.builder()
                        .orderCode(order.getOrderCode())
                        .orderDate(order.getOrderDate())
                        .orderStatus(order.getStatus())
                        .address(address)
                        .orderDetails(order.getOrderDetails().stream().map(od -> {
                            Set<ProductImg> productImgs = od.getProductCode().getProductImgs();
                            ProductImg productImg = productImgs.stream()
                                    .filter(pi -> pi.getMain() == 0)
                                    .findFirst().orElse(null);
                            return OrderDetailResponseDto.builder()
                                    .productName(od.getProductCode().getName())
                                    .productImg(productImg != null ? fileUploadService.getS3(productImg.getImgKey()) : null)
                                    .quantity(od.getQuantity())
                                    .orderAmount(od.getOrderAmount())
                                    .totalPrice(od.getTotalPrice())
                                    .build();
                        }).collect(Collectors.toList()))
                        .build();
            });

            return ResponseEntity.ok(orderResponsePage);
        } catch (Exception e) {
            log.error(Constants.ORDER_GET_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.ORDER_GET_FAIL);
        }
    }

    @Override
    public ResponseEntity<String> requestRefund(RefundRequestDto refundRequestDto) {
        // order, payment entity 가져오기
        Order order = orderRepository.findByOrderCode(refundRequestDto.getOrderCode())
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_ORDER));
        Payment payment = paymentRepository.findByOrderCode(refundRequestDto.getOrderCode())
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_PAYMENT));

        // iamport 환불 요청
        String url = "https://api.portone.io/payments/" + payment.getPaymentCode() + "/cancel";
        log.info("Payment URL: {}", url);
        HttpResponse<String> cancelResponse = Unirest.post("https://api.portone.io/payments/" + payment.getPaymentCode() + "/cancel")
                .header("Authorization", "PortOne " + secret)
                .header("Content-Type", "application/json")
                .body("{\"reason\":\"" + refundRequestDto.getReason() + "\"}")

                .asString();
        if(!cancelResponse.isSuccess()){
            log.error("Failed to process payment: Status={}, Body={}",
                    cancelResponse.getStatus(), cancelResponse.getBody());
        }
        //payment정보 수정(결제 취소), order정보 수정, refund 정보 insert
        switch (cancelResponse.getStatus()) {
            case 200 -> {
                payment.setStatus(Payment.Status.결제취소);
                paymentRepository.save(payment);

                if(order.getStatus().equals(Order.Status.결제완료)){
                    order.setStatus(Order.Status.주문취소);
                    orderRepository.save(order);

                    Refund refund = Refund.builder()
                            .refundCode(commonUtil.createCode())
                            .paymentCode(payment)
                            .refundDate(LocalDateTime.now())
                            .build();
                    refundRepository.save(refund);

                    return ResponseEntity.ok(Constants.PAYMENT_CANCEL_SUCCESS);
                } else {
                    log.error(Constants.ALREADY_DELIEVERED);
                    return ResponseEntity.badRequest().body(Constants.ALREADY_DELIEVERED);
                }
            }
            case 401 -> {
                log.error(Constants.JWT_INVALID);
                return ResponseEntity.badRequest().body(Constants.JWT_INVALID);
            }
            case 403 -> {
                log.error(Constants.FORBIDDEN_ERROR);
                return ResponseEntity.badRequest().body(Constants.FORBIDDEN_ERROR);
            }
            case 404 -> {
                log.error(Constants.ORDER_PAYMENT_NOT_FOUND);
                return ResponseEntity.badRequest().body(Constants.ORDER_PAYMENT_NOT_FOUND);
            }
            case 409 -> {
                log.error(Constants.PAYMENT_ALREADY_CANCELLDED_ERROR);
                return ResponseEntity.badRequest().body(Constants.PAYMENT_ALREADY_CANCELLDED_ERROR);
            }
            case 502 -> {
                log.error(Constants.ORDER_PG);
                return ResponseEntity.badRequest().body(Constants.ORDER_PG);
            }
            default -> {
                log.error(Constants.PAYMENT_ERROR);
                return ResponseEntity.badRequest().body(Constants.PAYMENT_ERROR);
            }
        }
    }

}
