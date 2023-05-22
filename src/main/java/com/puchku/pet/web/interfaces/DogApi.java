package com.puchku.pet.web.interfaces;

import com.puchku.pet.model.AddDogRequestDto;
import com.puchku.pet.model.entities.Dog;
import com.puchku.pet.model.entities.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dogs")
public interface DogApi {
    @GetMapping("/{dogId}")
    ResponseEntity<Dog> getDogDetails(@PathVariable long dogId);

    @GetMapping
    ResponseEntity<Object> getDogByService(@RequestParam(required = false) String service, @RequestParam(required = false) Long userId);

    @PostMapping
    ResponseEntity<Object> addDog(@RequestBody AddDogRequestDto dog);

    @PutMapping("/{dog_id}")
    ResponseEntity<Object> updateDog(@PathVariable("dog_id") Long dogId, @RequestBody Dog dog);

    @DeleteMapping("/{dog_id}")
    ResponseEntity<Object> deleteDog(@PathVariable("dog_id") Long dogId);

    @PostMapping("/addService")
    ResponseEntity<Object> addService(@RequestBody Service service);
}
