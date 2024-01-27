package com.puchku.pet.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "seller_details", schema="poochku")
public class SellerDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long sellerId;

    @Column(name = "breeders_license")
    private String breedersLicense;

    @Column(name = "status")
    private String status;

    @Column(name = "address")
    private String address;

    @Column(name = "bio")
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName="seller_id")
    @JsonIgnore
    private SellerEntity sellerEntity;

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getBreedersLicense() {
        return breedersLicense;
    }

    public void setBreedersLicense(String breedersLicense) {
        this.breedersLicense = breedersLicense;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public SellerEntity getSellerEntity() {
        return sellerEntity;
    }

    public void setSellerEntity(SellerEntity sellerEntity) {
        this.sellerEntity = sellerEntity;
    }
}
