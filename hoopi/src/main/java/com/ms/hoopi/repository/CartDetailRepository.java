package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Cart;
import com.ms.hoopi.model.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Query("SELECT cd FROM CartDetail cd WHERE cd.cartCode.cartCode = :cartCode AND cd.productCode.productCode = :productCode")
    Optional<CartDetail> findByCartCodeAndProductCode(String cartCode, String productCode);

    @Query("SELECT cd FROM CartDetail cd WHERE cd.cartCode = :cart")
    List<CartDetail> findAllByCartCode(Cart cart);

    @Query("DELETE CartDetail cd WHERE cd.cartCode.cartCode = :cartCode AND cd.productCode.productCode = :productCode")
    void deleteByCartCodeAndProductCode(String cartCode, String productCode);
}
