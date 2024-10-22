package com.ms.hoopi.service;

import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.dto.UserJoinDto;
import com.ms.hoopi.model.entity.Address;
import com.ms.hoopi.model.entity.User;
import com.ms.hoopi.repository.AddressRepository;
import com.ms.hoopi.repository.UserRepository;
import com.ms.hoopi.common.util.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JoinService {

    private static final Logger log = LoggerFactory.getLogger(JoinService.class);
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CommonUtil commonUtil;


    @Transactional
    public ResponseEntity<String> joinUser(UserJoinDto userJoinDto) {

        //db에 저장 - user 저장, user의 주소 저장
        try {
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
                    .email(userJoinDto.getEmail())
                    .build();

            userRepository.save(user);
            saveAddress(user, userJoinDto.getAddress(), userJoinDto.getZonecode());

            return ResponseEntity.ok(Constants.JOIN_SUCCESS);

        } catch(Exception e) {
            log.error(Constants.JOIN_FAIL, e);
            throw new RuntimeException(Constants.JOIN_FAIL);
        }
    }

    public void saveAddress(User user, String userAddress, String zonecode){
        try{
            //새로운 address 엔티티 생성
            Address address = Address.builder()
                    .addressCode(commonUtil.createCode())
                    .code(user)
                    .address(userAddress)
                    .state("Y")
                    .main("Y")
                    .postcode(zonecode)
                    .build();
            addressRepository.save(address);
        } catch (Exception e) {
            log.error(Constants.ADDRESS_NOT_STORED, e);
        }

    }



}
