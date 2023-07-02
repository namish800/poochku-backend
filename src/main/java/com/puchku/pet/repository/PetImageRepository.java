package com.puchku.pet.repository;

import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.PetImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PetImageRepository extends JpaRepository<PetImageEntity, Long>{

}
