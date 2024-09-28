package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Cart;
import com.ms.hoopi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.code.id = :id AND c.status = 'N'")
    Optional<Cart> findByUserCode(String id);

    @Query("SELECT c FROM Cart c WHERE c.code.id = :id AND c.status = 'N'")
    Optional<Cart> findByUserId(String id);

    @Modifying
    @Query("UPDATE Cart c SET c.status = 'Y' WHERE c.cartCode = :cartCode")
    void deleteByCartCode(String cartCode);

    @Query("SELECT c FROM Cart c WHERE c.cartCode = :cartCode AND c.status = 'N'")
    Optional<Cart> findByCartCode(String cartCode);

}
