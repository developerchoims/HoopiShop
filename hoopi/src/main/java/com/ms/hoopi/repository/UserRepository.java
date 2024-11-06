package com.ms.hoopi.repository;

import com.ms.hoopi.admin.user.model.dto.UserUpdateRequestDto;
import com.ms.hoopi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User save(User user);
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findById(@Param("id") String id);

    @Query("SELECT u FROM User u WHERE u.id LIKE CONCAT('%', :keyword, '%')")
    Page<User> searchAllById(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT('%', :keyword, '%')")
    Page<User> searchAllByName(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%', :keyword, '%')")
    Page<User> searchAllByEmail(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.phone LIKE CONCAT('%', :keyword, '%')")
    Page<User> searchAllByPhone(String keyword, Pageable pageable);

    Optional<User> findByCode(String code);

    @Query("SELECT u.id FROM User u WHERE u = :user")
    String findUserIdByUser(User user);

    @Modifying
    @Query("UPDATE User u SET u.quitYn = 'Y', u.quitDate = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateById(String id);

    @Modifying
    void save(UserUpdateRequestDto user);





}
