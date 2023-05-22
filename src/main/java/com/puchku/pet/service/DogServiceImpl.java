package com.puchku.pet.service;

import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.AddDogRequestDto;
import com.puchku.pet.model.entities.Dog;
import com.puchku.pet.model.entities.DogService;
import com.puchku.pet.model.entities.User;
import com.puchku.pet.repository.DogRepository;
import com.puchku.pet.repository.DogServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DogServiceImpl {
    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private DogServiceRepository dogServiceRepository;

    public ResponseEntity<Dog> getDogDetails(long dogId){

        Dog dog = dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new NotFoundException("Dog not found with ID: " + dogId));
        System.out.println(dog);
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    public ResponseEntity<Object> getDogByService(String serviceCode, Long userId){
        List<Dog> dogList = null;
        if(StringUtils.hasLength(serviceCode)){
            dogList = dogRepository.findByServiceCode(serviceCode);
        } else if(userId!=null){
            dogList = dogRepository.findByUserId(userId);
        }
        if(dogList!=null && dogList.isEmpty()){
            throw new NotFoundException("No dogs Found");
        }
        return new ResponseEntity<>(dogList, HttpStatus.OK);
    }

    public ResponseEntity<Object> addDog(AddDogRequestDto dog) {
        System.out.println(dog);
        Dog dogEntity = mapAddDogDtoToDogEntity(dog);
        dogEntity = dogRepository.save(dogEntity);
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    private Dog mapAddDogDtoToDogEntity(AddDogRequestDto dog) {
        Dog dogEntity = new Dog();
        dogEntity.setDogId(dog.getDogId());
        dogEntity.setBreed(dog.getBreed());
        dogEntity.setName(dog.getName());
        dogEntity.setDescription(dog.getDescription());
        User user = new User();
        user.setUserId(dog.getUserId());
        dogEntity.setUser(user);
        return dogEntity;
    }

    public ResponseEntity<Object> addService(com.puchku.pet.model.entities.Service service) {
        DogService dogService = new DogService();
        Dog dog = dogRepository.findById(service.getDogId()).get();
        dogService.setDog(dog);
        dogService.setServiceCode(service.getServiceCode());
        dogService.setServiceName(service.getServiceCode());

        DogService dogServiceEntity = dogServiceRepository.save(dogService);
        return new ResponseEntity<>(dogServiceEntity, HttpStatus.OK);
    }
}
