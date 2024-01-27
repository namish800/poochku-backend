package com.puchku.pet.service;

import com.puchku.pet.enums.SellerStatus;
import com.puchku.pet.enums.ServiceCode;
import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.Pet;
import com.puchku.pet.model.UserDto;
import com.puchku.pet.model.UserResponseDto;
import com.puchku.pet.model.UserResponseDtoPets;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.SellerDetailsEntity;
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
import java.util.stream.Collectors;

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
        if(StringUtils.isNotBlank(userDto.getEmail())){
            sellerEntity.setEmail(userDto.getEmail());
        }
        if(StringUtils.isNotBlank(userDto.getfName())){
            sellerEntity.setfName(userDto.getfName());
        }
        if(StringUtils.isNotBlank(userDto.getlName())) {
            sellerEntity.setlName(userDto.getlName());
        }
        if(StringUtils.isNotBlank(userDto.getPhoneNo())) {
            sellerEntity.setPhoneNo(userDto.getPhoneNo());
        }
        if(StringUtils.isNotBlank(userDto.getRole())) {
            sellerEntity.setRoles(List.of(userDto.getRole()));
        }

        if(StringUtils.isNotBlank(userDto.getPassword())) {
            sellerEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if(userDto.getRole().equalsIgnoreCase("seller")){
            SellerDetailsEntity sellerDetailsEntity = new SellerDetailsEntity();
            sellerDetailsEntity.setStatus(SellerStatus.UNVERIFIED.name());
            sellerEntity.setSellerDetailsEntity(sellerDetailsEntity);
        }

        sellerEntity.setLastLogin(OffsetDateTime.now());
        sellerEntity.setCrtTs(OffsetDateTime.now());
    }

    public ResponseEntity<UserResponseDto> getUserDetails(Integer userId) {
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
        UserResponseDto userDtoResponse = new UserResponseDto();
        createUserResponseDtoFromSellerEntity(sellerEntity, userDtoResponse);

        return new ResponseEntity<>(userDtoResponse, HttpStatus.OK);
    }

    private void createUserResponseDtoFromSellerEntity(SellerEntity sellerEntity, UserResponseDto userDtoResponse) {
        userDtoResponse.setUserId(sellerEntity.getSellerId());
        userDtoResponse.setEmail(sellerEntity.getEmail());
        userDtoResponse.setPhoneNo(sellerEntity.getPhoneNo());
        userDtoResponse.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        userDtoResponse.setfName(sellerEntity.getfName());
        userDtoResponse.setlName(sellerEntity.getlName());
        userDtoResponse.setRole(sellerEntity.getRoles().get(0));
        if(sellerEntity.getSellerDetailsEntity().isPresent()) {
            SellerDetailsEntity sellerDetailsEntity = sellerEntity.getSellerDetailsEntity().get();
            userDtoResponse.setAddress(sellerDetailsEntity.getAddress());
            userDtoResponse.setBio(sellerDetailsEntity.getBio());
            userDtoResponse.setBreedersLicense(sellerDetailsEntity.getBreedersLicense());
            userDtoResponse.setSellerStatus(sellerDetailsEntity.getStatus());
        }
        userDtoResponse.setPets(createUserResponsePetsFromPetEntity(sellerEntity.getPetEntityList()));
    }

    private UserResponseDtoPets createUserResponsePetsFromPetEntity(List<PetEntity> petEntityList) {
        if(petEntityList.isEmpty()) return new UserResponseDtoPets();

        UserResponseDtoPets userResponseDtoPets = new UserResponseDtoPets();
        List<Pet> pets_for_selling = petEntityList.stream()
                .filter(x -> x.getService().getServiceCode().equalsIgnoreCase(ServiceCode.SELLING.getCode()))
                .map(CommonUtils::mapPetEntitytoPetResponse)
                .toList();

        List<Pet> pets_for_adoption = petEntityList.stream()
                .filter(x -> x.getService().getServiceCode().equalsIgnoreCase(ServiceCode.ADOPTION.getCode()))
                .map(CommonUtils::mapPetEntitytoPetResponse)
                .toList();

        List<Pet> pets_for_mating = petEntityList.stream()
                .filter(x -> x.getService().getServiceCode().equalsIgnoreCase(ServiceCode.MATING.getCode()))
                .map(CommonUtils::mapPetEntitytoPetResponse)
                .toList();

        userResponseDtoPets.setPetsForSell(pets_for_selling);
        userResponseDtoPets.setPetsForAdoption(pets_for_adoption);
        userResponseDtoPets.setPetsForMating(pets_for_mating);
        return userResponseDtoPets;
    }

    private void createSellerDtoFromSellerEntity(SellerEntity sellerEntity, UserDto userDto) {
        userDto.setUserId(sellerEntity.getSellerId());
        userDto.setEmail(sellerEntity.getEmail());
        userDto.setPhoneNo(sellerEntity.getPhoneNo());
        userDto.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        userDto.setfName(sellerEntity.getfName());
        userDto.setlName(sellerEntity.getlName());
        userDto.setRole(sellerEntity.getRoles().get(0));
    }

    public ResponseEntity<UserDto> updateUserDetails(UserDto userDto) {
        if(userDto!=null && (userDto.getUserId()==0 || userDto.getUserId()==null)){
            throw new BadRequestException("Missing user id");
        }
        Optional<SellerEntity> userEnityOpt = sellerRepository.findBySellerId(userDto.getUserId());
        SellerEntity userEntity = null;
        if(userEnityOpt.isEmpty()){
            throw new NotFoundException("Seller not found");
        } else {
            userEntity = userEnityOpt.get();
        }
        createSellerEntityFromSellerDto(userEntity, userDto);
        //update userEntity
        userEntity = sellerRepository.save(userEntity);

        createSellerDtoFromSellerEntity(userEntity, userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
