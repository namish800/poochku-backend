package com.puchku.pet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.puchku.pet.model.entities.User;
import jakarta.persistence.*;

public class AddDogRequestDto {
    private long dogId;
    private long userId;

    private String breed;


    private String name;


    private String description;

    public long getDogId() {
        return dogId;
    }

    public void setDogId(long dogId) {
        this.dogId = dogId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AddDogRequestDto{" +
                "dogId=" + dogId +
                ", userId=" + userId +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
