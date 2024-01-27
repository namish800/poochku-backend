package com.puchku.pet.repository;

import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.model.entities.SellerRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRatingEntity, Long> {
}
