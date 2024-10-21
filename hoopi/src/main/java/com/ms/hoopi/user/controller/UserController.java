package com.ms.hoopi.user.controller;

import com.ms.hoopi.repository.UserRepository;
import com.ms.hoopi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hoopi")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/personal-info")
    public ResponseEntity<?> getPersonalInfo(@RequestParam String id) {
        return userService.getPersonalInfo(id);
    }

    @PostMapping("/personal-info")
    public ResponseEntity<?> deletePersonalAddress(@RequestBody String addressCode) {
        return userService.deletePersonalAddress(addressCode);
    }
}
