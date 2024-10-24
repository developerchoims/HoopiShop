package com.ms.hoopi.cart.service.serviceImpl;

import com.ms.hoopi.cart.model.dto.CartPutRequestDto;
import com.ms.hoopi.cart.model.dto.CartRequestDto;
import com.ms.hoopi.cart.model.dto.CartResponseDto;
import com.ms.hoopi.cart.service.CartService;
import com.ms.hoopi.common.service.FileUploadService;
import com.ms.hoopi.common.util.CommonUtil;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.*;
import com.ms.hoopi.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CommonUtil commonUtil;
    private final FileUploadService fileUploadService;

    @Override
    public ResponseEntity<String> addCart(CartRequestDto cartRequestDto) {
        User user = userRepository.findById(cartRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_USER));

        Product product = productRepository.findByProductCode(cartRequestDto.getProductCode())
                .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_PRODUCT));

        try{
            Cart cart = cartRepository.findByUserCode(cartRequestDto.getId()).orElse(null);
            // 주문 상태가 N인 user정보가 같은 Cart 정보가 없는 경우
            if (cart == null) {
                // cart 정보 생성
                String cartCode = commonUtil.createCode();
                Cart newCart = Cart.builder()
                        .cartCode(cartCode)
                        .code(user)
                        .status("N")
                        .build();
                // cart 정보 저장
                cartRepository.save(newCart);

                // cartDetail 정보 생성
                CartDetailId cartDetailId = CartDetailId.builder()
                        .cartCode(cartCode)
                        .productCode(cartRequestDto.getProductCode())
                        .build();
                CartDetail cartDetail = CartDetail.builder()
                        .id(cartDetailId)
                        .cartCode(newCart)
                        .productCode(product)
                        .quantity(cartRequestDto.getQuantity())
                        .cartAmount(cartRequestDto.getCartAmount())
                        .build();
                // cartDetail 정보 저장
                cartDetailRepository.save(cartDetail);
            } else {
            // 주문 상태가 N인 user정보가 같은 Cart 정보가 있는 경우
                CartDetail cartDetail = cartDetailRepository.findByCartCodeAndProductCode(cart.getCartCode(),cartRequestDto.getProductCode())
                        .orElse(null);
                // cartDetail 정보에 같은 상품이 담기지 않은 경우
                if(cartDetail == null) {
                    CartDetailId cartDetailId = CartDetailId.builder()
                            .cartCode(cart.getCartCode())
                            .productCode(cartRequestDto.getProductCode())
                            .build();
                    CartDetail newCartDetail = CartDetail.builder()
                            .id(cartDetailId)
                            .cartCode(cart)
                            .productCode(product)
                            .quantity(cartRequestDto.getQuantity())
                            .cartAmount(cartRequestDto.getCartAmount())
                            .build();
                    // cartDetail 정보 저장
                    cartDetailRepository.save(newCartDetail);
                } else {
                // cartDetail 정보에 같은 상품이 담겨있는 경우
                    Long quantity = cartDetail.getQuantity() + cartRequestDto.getQuantity();
                    Long cartAmount = cartDetail.getCartAmount() + cartRequestDto.getCartAmount();
                    CartDetail newCartDetail = CartDetail.builder()
                            .id(cartDetail.getId())
                            .cartCode(cartDetail.getCartCode())
                            .productCode(cartDetail.getProductCode())
                            .quantity(quantity)
                            .cartAmount(cartAmount)
                            .build();
                    // cartDetail 정보 수정
                    cartDetailRepository.save(newCartDetail);
                }
            }
            return ResponseEntity.ok(Constants.CART_ADD_SUCCESS);
        } catch (Exception e){
            log.error(Constants.CART_ADD_FAIL,e);
            return ResponseEntity.badRequest().body(Constants.CART_ADD_FAIL);
        }
    }

    @Override
    public ResponseEntity<?> getCart(String id) {
        // 장바구니 정보 가져오기
        Cart cart = cartRepository.findByUserId(id)
                .orElse(null);
        // 장바구니에 담긴 게 있는 경우
        List<CartResponseDto> cartResponseDtos = new ArrayList<>();
        if(cart != null){
            List<CartDetail> cartDetails = cartDetailRepository.findAllByCartCode(cart);
            for(CartDetail cartDetail : cartDetails){
                //이미지 가져오기
                String imgKey = productImgRepository.findByProductCode(cartDetail.getProductCode().getProductCode()).getImgKey();
                String imgUrl = fileUploadService.getS3(imgKey);
                CartResponseDto cartResponseDto = CartResponseDto.builder()
                        .cartCode(cart.getCartCode())
                        .productCode(cartDetail.getProductCode().getProductCode())
                        .name(cartDetail.getProductCode().getName())
                        .quantity(cartDetail.getQuantity())
                        .cartAmount(cartDetail.getCartAmount())
                        .imgUrl(imgUrl)
                        .build();
                cartResponseDtos.add(cartResponseDto);
            }
        }

        // 유저 주소록 가져오기
        List<Address> addresses = userRepository.findById(id).get().getAddresses().stream().toList();
        Map<String, List> map = new HashMap<>();
        map.put("addresses", addresses);
        map.put("carts", cartResponseDtos.isEmpty() ? null : cartResponseDtos);
        log.info("map: {} ", map);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<String> updateCart(CartPutRequestDto cartPutRequestDto) {
        try{
            // 기존 cartDetail 정보 불러오기
            CartDetail cartDetail = cartDetailRepository.findByCartCodeAndProductCode(cartPutRequestDto.getCartCode(), cartPutRequestDto.getProductCode())
                    .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_CART));
            // 수정된 cartDetail 정보
            CartDetail newCartDetail = CartDetail.builder()
                                                .id(cartDetail.getId())
                                                .cartCode(cartDetail.getCartCode())
                                                .productCode(cartDetail.getProductCode())
                                                .quantity(cartPutRequestDto.getQuantity())
                                                .cartAmount(cartPutRequestDto.getCartAmount())
                                                .build();
            // 수정된 cartDetail 정보로 수정하기
            cartDetailRepository.save(newCartDetail);
            return ResponseEntity.ok(Constants.CART_UPDATE_SUCCESS);
        } catch (Exception e){
            log.error(Constants.CART_UPDATE_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.CART_UPDATE_FAIL);
        }
    }

    @Override
    public ResponseEntity<String> deleteCartPart(String cartCode, List<String> productCodes) {
        try{
            for(String productCode : productCodes){
                cartDetailRepository.deleteByCartCodeAndProductCode(cartCode, productCode);
            }
            return ResponseEntity.ok(Constants.CART_DELETE_SUCCESS);
        }catch (Exception e){
            log.error(Constants.CART_DELETE_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.CART_DELETE_FAIL);
        }
    }

    @Override
    public ResponseEntity<String> deleteCartAll(String cartCode) {
        try{
            // cartDetail 삭제
           cartDetailRepository.deleteByCartCode(cartCode);
           // cart status 'Y'로 변경
           cartRepository.deleteByCartCode(cartCode);
           return ResponseEntity.ok(Constants.CART_DELETE_SUCCESS);
        } catch (Exception e){
            log.error(Constants.CART_DELETE_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.CART_DELETE_FAIL);
        }
    }


}
