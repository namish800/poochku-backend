package com.puchku.pet.repository;

import com.puchku.pet.model.entities.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    @Query("SELECT d FROM Dog d JOIN DogService ds ON d.dogId=ds.dog.dogId WHERE ds.serviceCode=:serviceCode")
    List<Dog> findByServiceCode(String serviceCode);

    @Query("SELECT dog FROM Dog dog JOIN UserEntity user ON dog.user.userId=user.userId WHERE user.userId=:userId")
    List<Dog> findByUserId(Long userId);

    Optional<Dog> findByDogId(long dogId);

}
