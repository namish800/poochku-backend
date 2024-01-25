package com.puchku.pet.web;

import com.puchku.pet.model.CreateNewPetReqDto;
import com.puchku.pet.model.PaginatedPetResponseDto;
import com.puchku.pet.model.Pet;
import com.puchku.pet.service.PetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PetApiController implements com.puchku.pet.api.PetApi {

    @Autowired
    private PetServiceImpl petService;

    @Override
    public ResponseEntity<CreateNewPetReqDto> addNewPet(CreateNewPetReqDto petReqDto) {
        return petService.createNewPet(petReqDto);
    }

    @Override
    public ResponseEntity<String> deletePetDetails(Integer petId) {
        return petService.deletePet(petId);
    }

    @Override
    public ResponseEntity<PaginatedPetResponseDto> getPetByParams(String serviceCode, String location, String breed, String gender, String quality, Long userId, Integer page, Integer size) {
        return petService.getPetByParams(serviceCode, location, breed, gender, quality, userId, page, size);
    }

    @Override
    public ResponseEntity<PaginatedPetResponseDto> getPetByService(String serviceCode, Integer page, Integer size, String userId) {
        return petService.getPetByService(serviceCode, page, size, userId);
    }

    @Override
    public ResponseEntity<Pet> requestPetInfo(Integer petId) {
        return petService.requestPetInfo(petId);
    }

    @Override
    public ResponseEntity<CreateNewPetReqDto> updatePetDetails(CreateNewPetReqDto createNewPetReqDto) {
        return petService.updatePetDetails(createNewPetReqDto);
    }

}
