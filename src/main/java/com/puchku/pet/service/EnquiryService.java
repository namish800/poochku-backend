package com.puchku.pet.service;

import com.puchku.pet.enums.EnquiryType;
import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.entities.EnquiryEntity;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.repository.EnquiryRepository;
import com.puchku.pet.repository.PetRepository;
import com.puchku.pet.repository.SellerRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnquiryService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private EnquiryRepository enquiryRepository;

    public ResponseEntity<String> addNewEnquiry(String userId, String petId, EnquiryType enquiryType) {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(petId)){
            throw new BadRequestException("Missing userId or petId");
        }
        Optional<SellerEntity> sellerEntity = sellerRepository.findBySellerId(Long.parseLong(userId));
        if(sellerEntity.isEmpty()){
            throw new NotFoundException("User not found");
        }

        Optional<PetEntity> petEntity = petRepository.findByPetId(Long.parseLong(petId));
        if(petEntity.isEmpty()){
            throw new NotFoundException("Pet not found");
        }

        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setPetEntity(petEntity.get());
        enquiryEntity.setSellerEntity(sellerEntity.get());
        enquiryEntity.setEnquiryType(enquiryType.name());

        enquiryRepository.save(enquiryEntity);
        return new ResponseEntity<>("Enquiry saved successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> addNewEnquiry(String userId, EnquiryType enquiryType) {
        if(StringUtils.isBlank(userId)){
            throw new BadRequestException("Missing userId");
        }
        Optional<SellerEntity> sellerEntity = sellerRepository.findBySellerId(Long.parseLong(userId));
        if(sellerEntity.isEmpty()){
            throw new NotFoundException("User not found");
        }

        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setSellerEntity(sellerEntity.get());
        enquiryEntity.setEnquiryType(enquiryType.name());

        enquiryRepository.save(enquiryEntity);
        return new ResponseEntity<>("Enquiry saved successfully", HttpStatus.OK);
    }
}
