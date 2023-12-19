package com.puchku.pet.repository;

import com.puchku.pet.model.entities.PetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long>, JpaSpecificationExecutor<PetEntity> {

    @Query("SELECT pet FROM PetEntity pet JOIN PetServiceEntity petService ON pet.petId=petService.petEntity.petId WHERE petService.serviceCode=:serviceCode")
    Page<PetEntity> findByServiceCode(String serviceCode, Pageable pageable);

    Optional<PetEntity> findByPetId(long petId);

    Optional<PetEntity> findByPetIdAndService_serviceCode(long petId, String code);

    Page<PetEntity> findByService_serviceCodeAndSeller_sellerIdNot(String serviceCode, long sellerId, Pageable pageable);

}
