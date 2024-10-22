package com.ms.hoopi.user.service;

import com.ms.hoopi.user.model.dto.AddressRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getPersonalInfo(String id);

    ResponseEntity<?> deletePersonalAddress(String addressCode);

    ResponseEntity<?> addPersonalAddress(AddressRequestDto address);

    ResponseEntity<?> editMainAddress(String addressCode);
}
