package com.puchku.pet.service;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.puchku.pet.enums.PetStatus;
import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.InternalServerErrorException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.CreateNewPetReqDto;
import com.puchku.pet.model.CreateNewPetReqDtoImageBlobsInner;
import com.puchku.pet.model.PaginatedPetResponseDto;
import com.puchku.pet.model.Pet;
import com.puchku.pet.model.PetService;
import com.puchku.pet.model.UserDto;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.PetImageEntity;
import com.puchku.pet.model.entities.PetServiceEntity;
import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.repository.*;
import com.puchku.pet.util.CommonUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
    private SellerRepository sellerRepository;

    @Autowired
    private PetServiceRepository petServiceRepository;

    @Autowired
    private PetImageRepository petImageRepository;

    @Value("${azure.storage.connectionString}")
    private String connectionString;

    @Value("${azure.storage.containerName}")
    private String containerName;

    Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

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
        petResponse.setName(petEntity.getName());
        petResponse.setBreed(petEntity.getBreed());
        petResponse.setDescription(petEntity.getDescription());
        petResponse.setStatus(petEntity.getPetStatus());
        petResponse.setFatherBreed(petEntity.getFatherBreed());
        petResponse.setMotherBreed(petEntity.getMotherBreed());
        petResponse.setLocation(petEntity.getLocation());
        petResponse.setQuality(petEntity.getQuality());
        petResponse.setGender(petEntity.getGender());
        UserDto owner = mapSellerEntitytoUserDto(petEntity.getSeller());
        petResponse.setOwner(owner);
        petResponse.setService(mapPetServiceEntityToPetService(petEntity.getService()));
        if(petEntity.getPetImageEntity()!=null) {
            petResponse.setImageUrls(petEntity.getPetImageEntity().getImageUrls());
        }
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

    private UserDto mapSellerEntitytoUserDto(SellerEntity sellerEntity) {
        UserDto userDto = new UserDto();
        userDto.setUserId(sellerEntity.getSellerId());
        userDto.setfName(sellerEntity.getfName());
        userDto.setlName(sellerEntity.getlName());
        userDto.setEmail(sellerEntity.getEmail());
        userDto.setPhoneNo(sellerEntity.getPhoneNo());
        userDto.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        return userDto;
    }

    public ResponseEntity<com.puchku.pet.model.PaginatedPetResponseDto> getPetByService(String serviceCode, Integer page, Integer size, String userId) {
        Page<PetEntity> petEntityPage = null;
        List<PetEntity> petEntityList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        if(!StringUtils.hasLength(serviceCode)){
            throw new BadRequestException("Missing required parameters");
        }
        if(userId == null){
            petEntityPage = petRepository.findByServiceCode(serviceCode, pageable);
        } else {
            long sellerId = Long.parseLong(userId);
            petEntityPage = petRepository.findByService_serviceCodeAndSeller_sellerIdNot(serviceCode, sellerId, pageable);
        }

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
    @Transactional
    public ResponseEntity<CreateNewPetReqDto> createNewPet(CreateNewPetReqDto petReqDto) {
        if(petReqDto!=null && petReqDto.getOwnerId()!=null && petReqDto.getOwnerId()==null){
            throw new BadRequestException("Missing requcreateired owner id");
        }
        if(petReqDto!=null) {
            //add new pet in db
            PetEntity petEntity = new PetEntity();
            mapPetReqDtoToPetEntity(petEntity, petReqDto);
            petEntity = petRepository.save(petEntity);
            petReqDto.setPetId(String.valueOf(petEntity.getPetId()));

            //add the service for the pet based on service code
            PetServiceEntity petServiceEntity = new PetServiceEntity();
            petServiceEntity.setPetEntity(petEntity);
            petServiceEntity.setServiceCode(petReqDto.getServiceCode());
            petServiceEntity.setServiceName("SELL");
            if(petReqDto.getPrice()!=null) {
                petServiceEntity.setPrice(Integer.parseInt(petReqDto.getPrice()));
            }
            petServiceRepository.save(petServiceEntity);

            List<String> imageUrls = null;
            //upload to azure blob
            try {
                imageUrls = uploadImage(petReqDto.getImageBlobs());
            } catch (Exception e){
                logger.error("Upload Image failed with exception: ", e);
                throw new InternalServerErrorException("Upload Image failed with exception: " + e.getMessage());
            }
            petReqDto.setImageUrls(imageUrls);
            //to reduce response size
            petReqDto.setImageBlobs(null);
            PetImageEntity petImageEntity = new PetImageEntity();
            petImageEntity.setImageUrls(imageUrls);
            petImageEntity.setPetEntity(petEntity);
            petImageRepository.save(petImageEntity);
        }

        return new ResponseEntity<>(petReqDto, HttpStatus.OK);
    }

    private List<String> uploadImage(@Valid List<CreateNewPetReqDtoImageBlobsInner> imageBlobs) {
        // Create a BlobServiceClient instance
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(this.connectionString).buildClient();

        // Create a BlobContainerClient instance
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(this.containerName);

        List<String> urlsList = new ArrayList<>();
        for(CreateNewPetReqDtoImageBlobsInner imageBlob : imageBlobs){
            // Create a BlobClient instance for uploading the image
            BlobClient blobClient = containerClient.getBlobClient("vFolder/"+imageBlob.getFileName());
            InputStream dataStream = new ByteArrayInputStream(imageBlob.getBlob());
            Response<BlockBlobItem> response = blobClient.uploadWithResponse(new BlobParallelUploadOptions(dataStream, imageBlob.getBlob().length), null, null);
            if(response.getStatusCode()==HttpStatus.CREATED.value()){
                urlsList.add(blobClient.getBlobUrl());
            }
        }
        return urlsList;
    }

    private void mapPetReqDtoToPetEntity(PetEntity petEntity, CreateNewPetReqDto petReqDto) {
        if(petReqDto!=null) {
            if(StringUtils.hasLength(petReqDto.getOwnerId())) {
                Optional<SellerEntity> sellerEntity = sellerRepository.findBySellerId(Long.parseLong(petReqDto.getOwnerId().trim()));
                if (sellerEntity.isEmpty()) {
                    throw new BadRequestException("User does not Exist");
                }
                petEntity.setSeller(sellerEntity.get());
            }
            petEntity.setBreed(petReqDto.getBreed());
            petEntity.setName(petReqDto.getName());
            petEntity.setDescription(petReqDto.getDescription());
            petEntity.setPetType(petReqDto.getPetType());
            if(petReqDto.getAgeInDays()!=null) {
                petEntity.setAge(Integer.parseInt(petReqDto.getAgeInDays().trim()));
            }
            petEntity.setPetStatus(PetStatus.ACTIVE.toString());
            petEntity.setFatherBreed(petReqDto.getFatherBreed());
            petEntity.setMotherBreed(petReqDto.getMotherBreed());
            petEntity.setVaccStatus(petReqDto.getVaccinationStatus());
            petEntity.setLocation(petReqDto.getLocation());
            petEntity.setGender(petReqDto.getGender());
        }
    }

    public ResponseEntity<PaginatedPetResponseDto> getPetByParams(String serviceCode, String location, String breed,
                                                                  String gender, String quality, Long userId, Integer page,
                                                                  Integer size) {
        Specification<PetEntity> spec = Specification.where(null);
        List<PetEntity> petEntityList = new ArrayList<>();

        if(StringUtils.hasLength(serviceCode)){
            spec = spec.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get("service").get("serviceCode"), serviceCode);
            });
        }

        if (StringUtils.hasLength(location)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("location")), location.toLowerCase()));
        }

        if (StringUtils.hasLength(breed)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("breed")), breed.toLowerCase()));
        }
        if (StringUtils.hasLength(gender)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("gender"), gender));
        }
        if (StringUtils.hasLength(quality)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("quality"), quality));
        }
        if(userId != null){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.notEqual(root.get("seller").get("sellerId"), userId));
        }

//        // Executing the query
        Pageable pageable = PageRequest.of(page, size);
        Page<PetEntity> petEntityPage = petRepository.findAll(spec, pageable);
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

    public ResponseEntity<String> deletePet(Integer petId) {
        if(petId==null || petId==0){
            throw new BadRequestException("Invalid pet Id");
        }
        Optional<PetEntity> petEntityOptional = petRepository.findByPetId(petId);
        if(petEntityOptional.isEmpty()){
            throw new NotFoundException("Pet not found");
        }
        PetEntity petEntity = petEntityOptional.get();
        petEntity.setPetStatus(PetStatus.INACTIVE.toString());
        petRepository.save(petEntity);
        return new ResponseEntity<>("Pet Deleted", HttpStatus.OK);
    }

    public ResponseEntity<CreateNewPetReqDto> updatePetDetails(CreateNewPetReqDto petReqDto) {
        if(petReqDto!=null && (petReqDto.getPetId()==null || Integer.parseInt(petReqDto.getPetId())==0)){
            throw new BadRequestException("Invalid pet Id");
        }

        if(petReqDto!=null) {
            Optional<PetEntity> petEntityOptional = petRepository.findByPetId(Integer.parseInt(petReqDto.getPetId()));
            if(petEntityOptional.isEmpty()){
                throw new NotFoundException("Pet not found");
            }
            //pet from db
            PetEntity petEntity = petEntityOptional.get();
            // update pet details
            mapPetReqDtoToPetEntity(petEntity, petReqDto);
            petEntity = petRepository.save(petEntity);
            petReqDto.setPetId(String.valueOf(petEntity.getPetId()));

            //add the service for the pet based on service code
            PetServiceEntity petServiceEntity = new PetServiceEntity();
            petServiceEntity.setPetServiceId(petEntity.getService().getPetServiceId());
            petServiceEntity.setPetEntity(petEntity);
            petServiceEntity.setServiceCode(petReqDto.getServiceCode());
            petServiceEntity.setServiceName("SELL");
            if(petReqDto.getPrice()!=null) {
                petServiceEntity.setPrice(Integer.parseInt(petReqDto.getPrice()));
            }
            petServiceRepository.save(petServiceEntity);

            //upload to azure blob
            List<String> imageUrls = null;
            if(petReqDto.getImageBlobs() != null){
                imageUrls = uploadImage(petReqDto.getImageBlobs());
            }
            petReqDto.setImageUrls(imageUrls);
            //to reduce response size
            petReqDto.setImageBlobs(null);
            PetImageEntity petImageEntity = new PetImageEntity();
            petImageEntity.setPetImageId(petEntity.getPetImageEntity().getPetImageId());
            petImageEntity.setImageUrls(imageUrls);
            petImageEntity.setPetEntity(petEntity);
            petImageRepository.save(petImageEntity);
        }

        return new ResponseEntity<>(petReqDto, HttpStatus.OK);
    }
}
