package com.ms.hoopi.product.controller;

import com.ms.hoopi.model.entity.Product;
import com.ms.hoopi.product.model.ProductDetailResponseDto;
import com.ms.hoopi.product.model.ProductResponseDto;
import com.ms.hoopi.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("hoopi")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product-new")
    public List<ProductDetailResponseDto> getProductsNew() {
        return productService.getProductsNew();
    }

    @GetMapping("/product-popular")
    public List<ProductDetailResponseDto> getProductsPopular() {
        return productService.getProductsPopular();
    }

    @GetMapping("/product")
    public Page<ProductDetailResponseDto> getProducts(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) String searchCate,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "user") String role) {
        return productService.getProduct(page, size, searchCate, keyword, role);
    }

    @GetMapping("/product/{productCode}")
    public ResponseEntity<?> getProductDetail(@PathVariable String productCode) {
        log.info("productCode:{}",productCode);
        return productService.getProductDetail(productCode);
    }

}
