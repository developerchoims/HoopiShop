package com.ms.hoopi.service;

import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.dto.UserJoinDto;
import com.ms.hoopi.model.entity.Address;
import com.ms.hoopi.model.entity.User;
import com.ms.hoopi.repository.AddressRepository;
import com.ms.hoopi.repository.UserRepository;
import com.ms.hoopi.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class JoinService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CommonUtil commonUtil;


    public ResponseEntity<String> joinUser(UserJoinDto userJoinDto) {
        //id가 이미 존재할 경우 exception 발생
        Optional<User> storedUser = userRepository.findById(userJoinDto.getId());
        if (storedUser.isPresent()) {
            throw new DuplicateKeyException(Constants.ALREADY_EXIST);
        }
        // 새로운 user 엔티티 생성
        User user = User.builder()
                .code(commonUtil.createCode())
                .id(userJoinDto.getId())
                .pwd(commonUtil.hashPwd(userJoinDto.getPwd()))
                .name(userJoinDto.getName())
                .birth(userJoinDto.getBirth())
                .phone(userJoinDto.getPhone())
                .build();

        //db에 저장 - user 저장, user의 주소 저장
        try{
            userRepository.save(user);
            saveAddress(user, userJoinDto.getAddress());
            return ResponseEntity.ok(Constants.JOIN_SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException(Constants.JOIN_FAIL);
        }
    }

    public void saveAddress(User user, String userAddress){
        //새로운 address 엔티티 생성
        Address address = Address.builder()
                .addressCode(commonUtil.createCode())
                .code(user)
                .address(userAddress)
                .build();
        addressRepository.save(address);
    }



}
