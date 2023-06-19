package com.puchku.pet.service;

import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.CreateNewPetReqDto;
import com.puchku.pet.model.PaginatedPetResponseDto;
import com.puchku.pet.model.Pet;
import com.puchku.pet.model.PetService;
import com.puchku.pet.model.User;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.PetServiceEntity;
import com.puchku.pet.model.entities.UserEntity;
import com.puchku.pet.repository.PetRepository;
import com.puchku.pet.repository.DogServiceRepository;
import com.puchku.pet.repository.PetServiceRepository;
import com.puchku.pet.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DogServiceRepository dogServiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetServiceRepository petServiceRepository;

    public ResponseEntity<Pet> requestPetInfo(long petId) {
        PetEntity petEntity = petRepository.findByPetId(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found with ID: " + petId));
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
        petResponse.setMotherBreed(petEntity.getMotherBreed());
        petResponse.setLocation(petEntity.getLocation());
        petResponse.setQuality(petEntity.getQuality());
        User user = mapUserEntityToUser(petEntity.getUser());
        petResponse.setUser(user);
//        List<PetService> serviceList = petEntity.getServiceList()
//                .stream()
//                .map(this::mapPetServiceEntityToPetService)
//                .toList();
        petResponse.setService(mapPetServiceEntityToPetService(petEntity.getService()));
        return petResponse;
    }

    private PetService mapPetServiceEntityToPetService(PetServiceEntity petServiceEntity) {
        PetService petService = new PetService();
        petService.setServiceId(petServiceEntity.getPetServiceId());
        petService.setServiceCode(petServiceEntity.getServiceCode());
        petService.setServiceName(petServiceEntity.getServiceName());
        petService.setPrice(petServiceEntity.getPrice());
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

    public ResponseEntity<CreateNewPetReqDto> createNewPet(CreateNewPetReqDto petReqDto) {
        if(petReqDto!=null && petReqDto.getOwnerId()==0 && petReqDto.getOwnerId()==null){
            throw new BadRequestException("Missing required owner id");
        }
        if(petReqDto!=null) {
            //add new pet in db
            PetEntity petEntity = mapPetReqDtoToPetEntity(petReqDto);
            petEntity = petRepository.save(petEntity);
            petReqDto.setPetId(petEntity.getPetId());

            //add the service for the pet based on service code
            PetServiceEntity petServiceEntity = new PetServiceEntity();
            petServiceEntity.setPetEntity(petEntity);
            petServiceEntity.setServiceCode(petReqDto.getServiceCode());
            petServiceEntity.setServiceName("SELL");
            petServiceRepository.save(petServiceEntity);
        }
        return new ResponseEntity<>(petReqDto, HttpStatus.OK);
    }

    private PetEntity mapPetReqDtoToPetEntity(CreateNewPetReqDto petReqDto) {
        PetEntity petEntity = new PetEntity();
        Optional<UserEntity> userEntity = userRepository.findByUserId(petReqDto.getOwnerId());
        if(userEntity.isEmpty()){ throw new BadRequestException("User does not Exist");}
        petEntity.setUser(userEntity.get());
        petEntity.setBreed(petReqDto.getBreed());
        petEntity.setName(petReqDto.getName());
        petEntity.setDescription(petReqDto.getDescription());
        petEntity.setPetType(petReqDto.getPetType());
        petEntity.setAge(petReqDto.getAgeInDays());
        petEntity.setPetStatus("ACTIVE");
        petEntity.setFatherBreed(petReqDto.getFatherBreed());
        petEntity.setMotherBreed(petReqDto.getMotherBreed());
        petEntity.setVaccStatus(petReqDto.getVaccinationStatus());
        return petEntity;
    }

    public ResponseEntity<PaginatedPetResponseDto> getPetByParams(String serviceCode, String location, String breed,
                                                                  String gender, String quality, Integer page,
                                                                  Integer size) {
        Specification<PetEntity> spec = Specification.where(null);

        if (serviceCode != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<PetEntity, PetServiceEntity> join = root.join("pet_id");
                return criteriaBuilder.equal(join.get("serviceCode"), serviceCode);
            });
        }
        if (location != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("location"), location));
        }

        if (breed != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("breed"), breed));
        }
        if (gender != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("gender"), gender));
        }
        if (breed != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("quality"), quality));
        }

//        // Executing the query
//        List<YourEntity> results = entityRepository.findAll(spec);
        return null;
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
