package com.ms.hoopi.product.service.serviceImpl;

import com.ms.hoopi.common.service.FileUploadService;
import com.ms.hoopi.model.entity.Article;
import com.ms.hoopi.model.entity.Product;
import com.ms.hoopi.model.entity.ProductImg;
import com.ms.hoopi.product.model.ProductDetailResponseDto;
import com.ms.hoopi.product.model.ProductResponseDto;
import com.ms.hoopi.product.service.ProductService;
import com.ms.hoopi.repository.ArticleRepository;
import com.ms.hoopi.repository.ProductImgRepository;
import com.ms.hoopi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final ArticleRepository articleRepository;
    private final FileUploadService fileUploadService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailResponseDto> getProductsNew() {
        List<ProductDetailResponseDto> productNew = new ArrayList<>();

        List<Product> product = productRepository.findAllNew();
        for(Product p : product){
            ProductResponseDto productDto = ProductResponseDto.builder()
                                                                .productCode(p.getProductCode())
                                                                .price(p.getPrice())
                                                                .name(p.getName())
                                                                .createdAt(p.getCreatedAt())
                                                                .build();

            ProductImg productImg = productImgRepository.findByProductCode(productDto.getProductCode());
            String imgUrl = fileUploadService.getS3(productImg.getImgKey());

            String boardContent = articleRepository.findByProductCode(productDto.getProductCode());

            ProductDetailResponseDto productDetailDto = ProductDetailResponseDto.builder()
                                                                                .product(productDto)
                                                                                .imgUrl(imgUrl)
                                                                                .boardContent(boardContent)
                                                                                .build();
            productNew.add(productDetailDto);
        }
        return productNew;
    }

    @Override
    public List<ProductDetailResponseDto> getProductsPopular() {
        List<ProductDetailResponseDto> productPopular = new ArrayList<>();

        List<Product> product = productRepository.findAllPopular();
        for(Product p : product){
            ProductResponseDto productDto = ProductResponseDto.builder()
                    .productCode(p.getProductCode())
                    .price(p.getPrice())
                    .name(p.getName())
                    .createdAt(p.getCreatedAt())
                    .build();

            ProductImg productImg = productImgRepository.findByProductCode(productDto.getProductCode());
            String imgUrl = fileUploadService.getS3(productImg.getImgKey());

            String boardContent = articleRepository.findByProductCode(productDto.getProductCode());

            ProductDetailResponseDto productDetailDto = ProductDetailResponseDto.builder()
                    .product(productDto)
                    .imgUrl(imgUrl)
                    .boardContent(boardContent)
                    .build();
            productPopular.add(productDetailDto);
        }
        return productPopular;
    }

    @Override
    public Page<ProductDetailResponseDto> getProduct(int page, int size, String searchCate, String keyword, String role) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductDetailResponseDto> details = new ArrayList<>();

        if (role.equals("admin") || role.equals("user")) {
            for (Product p : products) {
                ProductImg productImg = productImgRepository.findByProductCode(p.getProductCode());
                String imgUrl = fileUploadService.getS3(productImg.getImgKey());
                String boardContent = articleRepository.findByProductCode(p.getProductCode());
                ProductResponseDto product = ProductResponseDto.builder()
                        .productCode(p.getProductCode())
                        .price(p.getPrice()) // 이전 예제에서는 0으로 설정되었지만, 이 경우 원래 가격을 사용합니다.
                        .name(p.getName())
                        .createdAt(p.getCreatedAt())
                        .build();
                ProductDetailResponseDto productDetail = ProductDetailResponseDto.builder()
                        .product(product)
                        .imgUrl(imgUrl)
                        .boardContent(boardContent)
                        .build();
                details.add(productDetail);
            }
        } else {
            for (Product p : products) {
                ProductImg productImg = productImgRepository.findByProductCode(p.getProductCode());
                String imgUrl = fileUploadService.getS3(productImg.getImgKey());
                String boardContent = articleRepository.findByProductCode(p.getProductCode());
                ProductResponseDto product = ProductResponseDto.builder()
                        .productCode(p.getProductCode())
                        .price(0L)
                        .name(p.getName())
                        .createdAt(p.getCreatedAt())
                        .build();
                ProductDetailResponseDto productDetail = ProductDetailResponseDto.builder()
                        .product(product)
                        .imgUrl(imgUrl)
                        .boardContent(boardContent)
                        .build();
                details.add(productDetail);
            }
        }
        return new PageImpl<>(details, pageable, products.getTotalElements());
    }

}
