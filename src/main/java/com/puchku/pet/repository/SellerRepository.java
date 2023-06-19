package com.puchku.pet.repository;

import com.puchku.pet.model.entities.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
    SellerEntity findByEmail(String email);
    Optional<SellerEntity> findByPhoneNo(String phoneNo);

    Optional<SellerEntity> findBySellerId(long sellerId);
}
