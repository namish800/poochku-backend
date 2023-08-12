package com.puchku.pet.service;

import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.SellerDto;
import com.puchku.pet.model.UserDto;
import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.repository.SellerRepository;
import com.puchku.pet.util.CommonUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserDto> createUserAccount(UserDto userDto) {
        if(userDto!=null && (StringUtils.isEmpty(userDto.getPhoneNo()) || StringUtils.isEmpty(userDto.getEmail()))) {
            throw new BadRequestException("Missing phone no. or email");
        }
        SellerEntity sellerEntity = new SellerEntity();
        createSellerEntityFromSellerDto(sellerEntity, userDto);

        sellerEntity = sellerRepository.save(sellerEntity);

        createSellerDtoFromSellerEntity(sellerEntity, userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    private void createSellerEntityFromSellerDto(SellerEntity sellerEntity, UserDto userDto) {
        if(userDto.getUserId()!=null && sellerEntity.getSellerId()!=0){
            sellerEntity.setSellerId(userDto.getUserId());
        }
        sellerEntity.setEmail(userDto.getEmail());
        sellerEntity.setfName(userDto.getfName());
        sellerEntity.setlName(userDto.getlName());
        sellerEntity.setLastLogin(OffsetDateTime.now());
        sellerEntity.setCrtTs(OffsetDateTime.now());
        sellerEntity.setPhoneNo(userDto.getPhoneNo());
        sellerEntity.setRoles(List.of(userDto.getRole()));
        sellerEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    public ResponseEntity<UserDto> getUserDetails(Integer userId) {
        if(userId==null || userId==0){
            throw new BadRequestException("Missing seller Id");
        }
        Optional<SellerEntity> sellerEntityOpt = sellerRepository.findBySellerId(userId);
        SellerEntity sellerEntity = null;
        if(sellerEntityOpt.isEmpty()){
            throw new NotFoundException("Seller not found");
        } else {
            sellerEntity = sellerEntityOpt.get();
        }
        UserDto userDtoResponse = new UserDto();
        createSellerDtoFromSellerEntity(sellerEntity, userDtoResponse);

        return new ResponseEntity<>(userDtoResponse, HttpStatus.OK);
    }

    private void createSellerDtoFromSellerEntity(SellerEntity sellerEntity, UserDto userDto) {
        userDto.setUserId(sellerEntity.getSellerId());
        userDto.setEmail(sellerEntity.getEmail());
        userDto.setPhoneNo(sellerEntity.getPhoneNo());
        userDto.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        userDto.setfName(sellerEntity.getfName());
        userDto.setlName(userDto.getlName());
    }
}
