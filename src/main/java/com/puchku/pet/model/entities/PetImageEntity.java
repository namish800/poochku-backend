package com.puchku.pet.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "pet_images", schema="poochku")
public class PetImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_image_id")
    private long petImageId;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName="pet_id")
    @JsonIgnore
    private PetEntity petEntity;

    @Column(name = "url")
    private List<String> imageUrls;

    public long getPetImageId() {
        return petImageId;
    }

    public void setPetImageId(long petImageId) {
        this.petImageId = petImageId;
    }

    public PetEntity getPetEntity() {
        return petEntity;
    }

    public void setPetEntity(PetEntity petEntity) {
        this.petEntity = petEntity;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
