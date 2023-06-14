package com.puchku.pet.repository;

import com.puchku.pet.model.entities.PetServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogServiceRepository extends JpaRepository<PetServiceEntity, Long> {
}
