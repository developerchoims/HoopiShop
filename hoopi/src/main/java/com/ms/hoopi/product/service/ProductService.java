package com.ms.hoopi.product.service;

import com.ms.hoopi.model.entity.Product;
import com.ms.hoopi.product.model.ProductDetailResponseDto;
import com.ms.hoopi.product.model.ProductResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<ProductDetailResponseDto> getProductsNew();

    List<ProductDetailResponseDto> getProductsPopular();

    Page<ProductDetailResponseDto> getProduct(int page, int size, String searchCate, String keyword, String role);
}
