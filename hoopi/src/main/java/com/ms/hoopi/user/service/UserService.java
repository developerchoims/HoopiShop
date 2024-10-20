package com.ms.hoopi.user.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getPersonalInfo(String id);
}
