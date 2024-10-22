package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Address;
import com.ms.hoopi.model.entity.User;
import com.ms.hoopi.order.model.dto.OrderResponseDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.addressCode = :addressCode")
    Address findByAddressCode(String addressCode);

    @Modifying
    @Query("DELETE FROM Address a WHERE a.addressCode = :addressCode")
    void deleteByAddressCode(String addressCode);

    @Query("SELECT a FROM Address a WHERE a.code = :user")
    List<Address> findByCode(User user);
}
