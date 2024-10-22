package com.ms.hoopi.user.controller;

import com.ms.hoopi.repository.UserRepository;
import com.ms.hoopi.user.model.dto.AddressRequestDto;
import com.ms.hoopi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

@RestController
@RequestMapping("hoopi")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/personal-info")
    public ResponseEntity<?> getPersonalInfo(@RequestParam String id) {
        return userService.getPersonalInfo(id);
    }

    @DeleteMapping("/personal-info/{addressCode}")
    public ResponseEntity<?> deletePersonalAddress(@PathVariable String addressCode) {
        log.info("addressCode : {} ",addressCode);
        return userService.deletePersonalAddress(addressCode);
    }

    @PostMapping("/personal-info")
    public ResponseEntity<?> addPersonalAddress(@RequestBody AddressRequestDto address) {
        return userService.addPersonalAddress(address);
    }
}
