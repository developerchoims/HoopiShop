package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Order;
import com.ms.hoopi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.code = :user ORDER BY o.orderDate DESC")
    Page<Order> findAllByUserCode(User user, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.code = :user ORDER BY o.orderDate DESC")
    void findAllByUserCodeAndKeyWord(User user, String keyword, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderCode = :orderCode")
    Optional<Order> findByOrderCode(String orderCode);

}
