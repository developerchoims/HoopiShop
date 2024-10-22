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

import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
                    .addresses(user.getAddresses().stream()
                                .sorted(Comparator.comparing((Address a) -> !"Y".equals(a.getMain())).thenComparing(Address::getAddressName))
                                .map(address -> AddressResponseDto.builder()
                                    .addressName(address.getAddressName())
                                    .addressCode(address.getAddressCode())
                                    .address(address.getAddress())
                                    .addressPhone(address.getAddressPhone())
                                    .postCode(address.getPostcode())
                                    .main(address.getMain())
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
        try{
            Address address = addressRepository.findByAddressCode(addressCode);
            User user = address.getCode();
            Set<Address> addresses = user.getAddresses();
            // address가 하나뿐인 경우 삭제 불가능
            if(addresses.size() == 1){
                return ResponseEntity.ok(Constants.ADDRESS_JUST_ONE);
            }else {
                // address가 여러 개인 경우 삭제 가능
                addressRepository.deleteByAddressCode(addressCode);
                // main인 address를 삭제했을 경우
                if(address.getMain().equals("Y")){
                    // 나머지 address 중 하나를 main으로 수정함
                    List<Address> remainAddresses = addressRepository.findByCode(user);
                    remainAddresses.stream().findFirst().ifPresent(ad -> ad.setMain("Y"));
                }
                return ResponseEntity.ok(Constants.ADDRESS_DELETE_SUCCESS);
            }
        } catch (Exception e ) {
            log.error(Constants.ADDRESS_DELETE_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.ADDRESS_DELETE_FAIL);
        }
    }

    @Override
    public ResponseEntity<?> addPersonalAddress(AddressRequestDto address) {
        try{
            Address addressEntity = Address.builder()
                    .addressCode(commonUtil.createCode())
                    .code(userRepository.findById(address.getId()).orElseThrow(() -> new EntityNotFoundException(Constants.NONE_USER)))
                    .address(address.getAddress() + " " + address.getExtraAddress())
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

    @Override
    public ResponseEntity<?> editMainAddress(String addressCode) {
        try{
            Address address = addressRepository.findByAddressCode(addressCode);
            if(address.getMain().equals("Y")){
                return ResponseEntity.ok(Constants.ADDRESS_ALREADY_MAIN);
            }
            Set<Address> addresses = address.getCode().getAddresses();
            addresses.forEach(ad-> {
                if(ad.getAddressCode().equals(addressCode)){
                    ad.setMain("Y");
                    addressRepository.save(ad);
                } else {
                    ad.setMain("N");
                    addressRepository.save(ad);
                }
            });
            return ResponseEntity.ok(Constants.ADDRESS_MAIN_SUCCESS);
        } catch (Exception e){
            log.error(Constants.ADDRESS_MAIN_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.ADDRESS_MAIN_FAIL);
        }
    }
}
