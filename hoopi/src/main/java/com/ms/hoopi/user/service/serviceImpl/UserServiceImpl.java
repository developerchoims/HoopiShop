package com.ms.hoopi.user.service.serviceImpl;

import com.ms.hoopi.common.util.CommonUtil;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.Address;
import com.ms.hoopi.model.entity.User;
import com.ms.hoopi.repository.AddressRepository;
import com.ms.hoopi.repository.UserRepository;
import com.ms.hoopi.user.model.dto.AddressRequestDto;
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
    private final CommonUtil commonUtil;

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

    @Override
    public ResponseEntity<?> addPersonalAddress(AddressRequestDto address) {
        try{
            Address addressEntity = Address.builder()
                    .addressCode(commonUtil.createCode())
                    .code(userRepository.findByCode(address.getId()).orElseThrow(() -> new EntityNotFoundException(Constants.NONE_USER)))
                    .address(address.getAddress())
                    .addressPhone(address.getAddressPhone())
                    .addressName(address.getAddressName())
                    .postcode(address.getPostCode())
                    .build();
            addressRepository.save(addressEntity);
            return ResponseEntity.ok(Constants.ADDRESS_ADD_SUCCESS);
        }catch (Exception e){
            log.error(Constants.ADDRESS_ADD_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.ADDRESS_ADD_FAIL);
        }
    }
}
