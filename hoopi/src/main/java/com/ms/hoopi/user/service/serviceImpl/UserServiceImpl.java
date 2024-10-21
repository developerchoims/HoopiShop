package com.ms.hoopi.user.service.serviceImpl;

import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.User;
import com.ms.hoopi.repository.AddressRepository;
import com.ms.hoopi.repository.UserRepository;
import com.ms.hoopi.user.model.dto.AddressResponseDto;
import com.ms.hoopi.user.model.dto.UserResponseDto;
import com.ms.hoopi.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public ResponseEntity<?> getPersonalInfo(String id) {
        try{
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_USER));

            UserResponseDto userInfo = UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .addresses(user.getAddresses().stream().map(address -> AddressResponseDto.builder()
                                    .addressName(address.getAddressName())
                                    .addressCode(address.getAddressCode())
                                    .address(address.getAddress())
                                    .addressPhone(address.getAddressPhone())
                                    .postCode(address.getPostcode())
                                    .build())
                            .toList())
                    .build();
            return ResponseEntity.ok(userInfo);
        } catch (Exception e){
            log.error(Constants.NONE_USER, e);
            return ResponseEntity.badRequest().body(Constants.NONE_USER);
        }
    }

    @Override
    public ResponseEntity<?> deletePersonalAddress(String addressCode) {
        int result = addressRepository.deleteByAddressCode(addressCode);
        if(result > 0){
            return ResponseEntity.ok(Constants.ADDRESS_DELETE_SUCCESS);
        } else {
            return ResponseEntity.badRequest().body(Constants.ADDRESS_DELETE_FAIL);
        }
    }
}
