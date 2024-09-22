package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, String> {
    @Query(value = "SELECT * FROM product_img pi WHERE pi.product_code = :productCode AND pi.main = 0", nativeQuery = true)
    ProductImg findByProductCode(String productCode);

    @Query(value = "SELECT * FROM product_img pi WHERE pi.product_code = :productCode AND pi.main = 1", nativeQuery = true)
    ProductImg findBoardImgByProductCode(String productCode);
}
