package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Address;
import com.ms.hoopi.order.model.dto.OrderResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.addressCode = :addressCode")
    Address findByAddressCode(String addressCode);

    @Query("DELETE FROM Address a WHERE a.addressCode = :addressCode")
    int deleteByAddressCode(String addressCode);
}
