package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.code.id = :id AND c.status = 'N'")
    Optional<Cart> findByUserCode(String id);
}
