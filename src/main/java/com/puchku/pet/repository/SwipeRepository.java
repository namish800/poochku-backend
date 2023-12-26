package com.puchku.pet.repository;

import com.puchku.pet.model.entities.SwipeEntity;
import com.puchku.pet.model.projections.SwipeProjection;
import com.puchku.pet.model.projections.SwipeProjectionForTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwipeRepository extends JpaRepository<SwipeEntity, Long> {

    List<SwipeEntity> findBySwiper_petIdAndStatus(long swiper, String status);

    List<SwipeEntity> findByTarget_petIdAndStatus(long target, String status);

    Optional<SwipeEntity> findBySwiper_petIdAndTarget_petIdAndDirectionAndStatus(long swiper, long target, String dir, String status);

    Optional<SwipeEntity> findBySwiper_petIdAndTarget_petId(long swiper, long target);
}
