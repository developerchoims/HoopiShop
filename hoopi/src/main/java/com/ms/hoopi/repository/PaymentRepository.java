package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.orderCode.orderCode = :orderCode")
    Optional<Payment> findByOrderCode(String orderCode);
}
