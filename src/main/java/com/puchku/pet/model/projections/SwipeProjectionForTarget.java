package com.puchku.pet.model.projections;

import com.puchku.pet.model.entities.PetEntity;

public interface SwipeProjectionForTarget {
    Long getSwipeId();
    PetEntity getTarget();
    String getStatus();
}
