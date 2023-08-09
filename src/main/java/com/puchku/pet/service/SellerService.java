package com.puchku.pet.service;

import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.SellerDto;
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
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<SellerDto> createSellerAccount(SellerDto sellerDto) {
        if(sellerDto!=null && (StringUtils.isEmpty(sellerDto.getPhoneNo()) || StringUtils.isEmpty(sellerDto.getEmail()))) {
            throw new BadRequestException("Missing phone no. or email");
        }
        SellerEntity sellerEntity = new SellerEntity();
        createSellerEntityFromSellerDto(sellerEntity, sellerDto);

        sellerEntity = sellerRepository.save(sellerEntity);

        createSellerDtoFromSellerEntity(sellerEntity, sellerDto);
        return new ResponseEntity<>(sellerDto, HttpStatus.CREATED);
    }

    private void createSellerEntityFromSellerDto(SellerEntity sellerEntity, SellerDto sellerDto) {
        if(sellerDto.getSellerId()!=null && sellerEntity.getSellerId()!=0){
            sellerEntity.setSellerId(sellerDto.getSellerId());
        }
        sellerEntity.setEmail(sellerDto.getEmail());
        sellerEntity.setfName(sellerDto.getfName());
        sellerEntity.setlName(sellerDto.getlName());
        sellerEntity.setLastLogin(OffsetDateTime.now());
        sellerEntity.setCrtTs(OffsetDateTime.now());
        sellerEntity.setPhoneNo(sellerDto.getPhoneNo());
        sellerEntity.setRole("SELLER");
        sellerEntity.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
    }

    public ResponseEntity<SellerDto> getSellerDetails(Integer sellerId) {
        if(sellerId==null || sellerId==0){
            throw new BadRequestException("Missing seller Id");
        }
        Optional<SellerEntity> sellerEntityOpt = sellerRepository.findBySellerId(sellerId);
        SellerEntity sellerEntity = null;
        if(sellerEntityOpt.isEmpty()){
            throw new NotFoundException("Seller not found");
        } else {
            sellerEntity = sellerEntityOpt.get();
        }
        SellerDto sellerDtoResponse = new SellerDto();
        createSellerDtoFromSellerEntity(sellerEntity, sellerDtoResponse);

        return new ResponseEntity<>(sellerDtoResponse, HttpStatus.OK);
    }

    private void createSellerDtoFromSellerEntity(SellerEntity sellerEntity, SellerDto sellerDto) {
        sellerDto.setSellerId(sellerEntity.getSellerId());
        sellerDto.setEmail(sellerEntity.getEmail());
        sellerDto.setPhoneNo(sellerEntity.getPhoneNo());
        sellerDto.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        sellerDto.setfName(sellerEntity.getfName());
        sellerDto.setlName(sellerDto.getlName());
    }
}
