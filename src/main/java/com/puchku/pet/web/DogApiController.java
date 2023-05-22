package com.puchku.pet.web;

import com.puchku.pet.model.AddDogRequestDto;
import com.puchku.pet.model.entities.Dog;
import com.puchku.pet.model.entities.Service;
import com.puchku.pet.service.DogServiceImpl;
import com.puchku.pet.web.interfaces.DogApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DogApiController implements DogApi {
    @Autowired
    private DogServiceImpl dogService;

    @Override
    public ResponseEntity<Dog> getDogDetails(long dogId) {
        return dogService.getDogDetails(dogId);
    }

    @Override
    public ResponseEntity<Object> getDogByService(String service, Long userId){
        return dogService.getDogByService(service, userId);
    }

    @Override
    public ResponseEntity<Object> addDog(AddDogRequestDto dog) {
        return dogService.addDog(dog);
    }

    @Override
    public ResponseEntity<Object> updateDog(Long dogId, Dog dog) {
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteDog(Long dogId) {
        return new ResponseEntity<>(new Dog(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addService(Service service){
        return dogService.addService(service);
    }
}
