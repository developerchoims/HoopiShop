package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User save(User user);
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findById(@Param("id") String id);
}
