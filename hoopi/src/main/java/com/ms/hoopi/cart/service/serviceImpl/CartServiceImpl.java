package com.ms.hoopi.cart.service.serviceImpl;

import com.ms.hoopi.cart.model.dto.CartRequestDto;
import com.ms.hoopi.cart.service.CartService;
import com.ms.hoopi.common.util.CommonUtil;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.*;
import com.ms.hoopi.repository.CartDetailRepository;
import com.ms.hoopi.repository.CartRepository;
import com.ms.hoopi.repository.ProductRepository;
import com.ms.hoopi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository CartDetailRepository;
    private final ProductRepository productRepository;
    private final CommonUtil commonUtil;
    private final CartDetailRepository cartDetailRepository;

    @Override
    public ResponseEntity<String> addCart(CartRequestDto cartRequestDto) {
        User user = userRepository.findById(cartRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_USER));

        Product product = productRepository.findByProductCode(cartRequestDto.getProductCode())
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_PRODUCT));

        try{
            // cart 정보 생성
            String cartCode = commonUtil.createCode();
            Cart cart = Cart.builder()
                    .cartCode(cartCode)
                    .code(user)
                    .build();
            // cart 정보 저장
            cartRepository.save(cart);

            // cartDetail 정보 생성
            CartDetailId cartDetailId = CartDetailId.builder()
                    .cartCode(cartCode)
                    .productCode(cartRequestDto.getProductCode())
                    .build();
            CartDetail cartDetail = CartDetail.builder()
                    .id(cartDetailId)
                    .cartCode(cart)
                    .productCode(product)
                    .quantity(cartRequestDto.getQuantity())
                    .cartAmount(cartRequestDto.getCartAmount())
                    .build();
            // cartDetail 정보 저장
            cartDetailRepository.save(cartDetail);
            return ResponseEntity.ok(Constants.CART_ADD_SUCCESS);
        } catch (Exception e){
            log.error(Constants.CART_ADD_FAIL,e);
            return ResponseEntity.badRequest().body(Constants.CART_ADD_FAIL);
        }

    }
}