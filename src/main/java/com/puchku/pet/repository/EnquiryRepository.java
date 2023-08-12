package com.puchku.pet.repository;

import com.puchku.pet.model.entities.EnquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryRepository extends JpaRepository<EnquiryEntity, Long> {
}
