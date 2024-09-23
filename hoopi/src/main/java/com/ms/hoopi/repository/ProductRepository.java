package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = "SELECT * FROM product p ORDER BY p.created_at DESC LIMIT 10", nativeQuery = true)
    List<Product> findAllNew();

    @Query(value = "SELECT p.* FROM product p LEFT JOIN order_detail o ON p.product_code = o.product_code GROUP BY p.product_code ORDER BY SUM(o.quantity) DESC LIMIT 10", nativeQuery = true)
    List<Product> findAllPopular();

    @Query("SELECT p FROM Product p WHERE p.name LIKE CONCAT('%',:keyword,'%') ")
    Page<Product> searchByNameKeyword(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN Article a ON p.productCode = a.productCode.productCode WHERE a.boardContent LIKE CONCAT('%',:keyword,'%')")
    Page<Product> searchByContentKeyword(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productCode = :productCode")
    Optional<Product> findByProductCode(String productCode);
}
