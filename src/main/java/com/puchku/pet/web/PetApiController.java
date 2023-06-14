package com.puchku.pet.web;

import com.puchku.pet.model.ListPetResponseDto;
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
    public ResponseEntity<Void> deletePetDetails(Integer petId) {
        return null;
    }

    @Override
    public ResponseEntity<PaginatedPetResponseDto> getPetByService(String serviceCode, Integer page, Integer size) {
        return petService.getPetByService(serviceCode, page, size);
    }

    @Override
    public ResponseEntity<Pet> requestPetInfo(Integer petId) {
        return petService.requestPetInfo(petId);
    }

    @Override
    public ResponseEntity<Pet> updatePetDetails(Integer petId) {
        return null;
    }
}
