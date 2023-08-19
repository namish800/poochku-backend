package com.puchku.pet.model.projections;

import com.puchku.pet.model.entities.PetEntity;

public interface SwipeProjection {
    Long getSwipeId();
    PetEntity getSwiper();
    String getStatus();
}
