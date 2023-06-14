package com.puchku.pet.service;

import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.*;
import com.puchku.pet.model.ListPetResponseDto;
import com.puchku.pet.model.PaginatedPetResponseDto;
import com.puchku.pet.model.Pet;
import com.puchku.pet.model.PetService;
import com.puchku.pet.model.User;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.PetServiceEntity;
import com.puchku.pet.model.entities.UserEntity;
import com.puchku.pet.repository.PetRepository;
import com.puchku.pet.repository.DogServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DogServiceRepository dogServiceRepository;

    public ResponseEntity<Pet> requestPetInfo(long petId) {
        PetEntity petEntity = petRepository.findByPetId(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found with ID: " + petId));
        System.out.println(petEntity);
        Pet response = mapPetEntitytoPetResponse(petEntity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Pet mapPetEntitytoPetResponse(PetEntity petEntity) {
        Pet petResponse = new Pet();
        petResponse.setPetId(petEntity.getPetId());
        petResponse.setAge(petEntity.getAge());
        petResponse.setPetType(petEntity.getPetType());
        petResponse.setName(petResponse.getName());
        petResponse.setBreed(petEntity.getBreed());
        petResponse.setDescription(petEntity.getDescription());
        petResponse.setStatus(petEntity.getPetStatus());
        petResponse.setFatherBreed(petEntity.getFatherBreed());
        petResponse.setMotherBreed(petResponse.getMotherBreed());
        User user = mapUserEntityToUser(petEntity.getUser());
        petResponse.setUser(user);
        List<PetService> serviceList = petEntity.getServiceList()
                .stream()
                .map(this::mapPetServiceEntityToPetService)
                .toList();
        petResponse.setService(serviceList);
        return petResponse;
    }

    private PetService mapPetServiceEntityToPetService(PetServiceEntity petServiceEntity) {
        PetService petService = new PetService();
        petService.setServiceId(petServiceEntity.getPetServiceId());
        petService.setServiceCode(petServiceEntity.getServiceCode());
        petService.setServiceName(petServiceEntity.getServiceName());
        return petService;
    }

    private User mapUserEntityToUser(UserEntity userEntity) {
        User user = new User();
        user.setUserId(userEntity.getUserId());
        user.setfName(userEntity.getfName());
        user.setlName(userEntity.getlName());
        user.setEmail(userEntity.getEmail());
        user.setPhoneNo(userEntity.getPhoneNo());
        return user;
    }

    public ResponseEntity<com.puchku.pet.model.PaginatedPetResponseDto> getPetByService(String serviceCode, Integer page, Integer size) {
        Page<PetEntity> petEntityPage = null;
        List<PetEntity> petEntityList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        if(!StringUtils.hasLength(serviceCode)){
            throw new BadRequestException("Missing required parameters");
        }
        petEntityPage = petRepository.findByServiceCode(serviceCode, pageable);
        petEntityList = petEntityPage.getContent();
        if (petEntityList.isEmpty()) {
            throw new NotFoundException("No Pets Found");
        }
        // creating the response
        PaginatedPetResponseDto response = new PaginatedPetResponseDto();
        List<Pet> petList = petEntityList.stream().map(this::mapPetEntitytoPetResponse).collect(Collectors.toList());
        response.setCurrentPage(page);
        response.setTotalItems(petEntityPage.getTotalElements());
        response.setTotalPages(petEntityPage.getTotalPages());
        response.setPets(petList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


//    public ResponseEntity<Object> addDog(AddDogRequestDto dog) {
//        System.out.println(dog);
//        PetEntity petEntityEntity = mapAddDogDtoToDogEntity(dog);
//        petEntityEntity = dogRepository.save(petEntityEntity);
//        return new ResponseEntity<>(dog, HttpStatus.OK);
//    }
//
//    private PetEntity mapAddDogDtoToDogEntity(AddDogRequestDto dog) {
//        PetEntity petEntityEntity = new PetEntity();
//        petEntityEntity.setDogId(dog.getDogId());
//        petEntityEntity.setBreed(dog.getBreed());
//        petEntityEntity.setName(dog.getName());
//        petEntityEntity.setDescription(dog.getDescription());
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserId(dog.getUserId());
//        petEntityEntity.setUser(userEntity);
//        return petEntityEntity;
//    }
//
//    public ResponseEntity<Object> addService(com.puchku.pet.model.entities.Service service) {
//        PetServiceEntity petServiceEntity = new PetServiceEntity();
//        PetEntity petEntity = dogRepository.findById(service.getDogId()).get();
//        petServiceEntity.setDog(petEntity);
//        petServiceEntity.setServiceCode(service.getServiceCode());
//        petServiceEntity.setServiceName(service.getServiceCode());
//
//        PetServiceEntity petServiceEntityEntity = dogServiceRepository.save(petServiceEntity);
//        return new ResponseEntity<>(petServiceEntityEntity, HttpStatus.OK);
//    }
}
