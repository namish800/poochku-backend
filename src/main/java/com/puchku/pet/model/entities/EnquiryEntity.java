package com.puchku.pet.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "enquiry", schema="poochku")
public class EnquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enquiry_id")
    private long enquiryId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="seller_id")
    @JsonIgnore
    private SellerEntity sellerEntity;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "pet_id")
    @JsonIgnore
    private PetEntity petEntity;

    @Column(name = "type")
    private String enquiryType;

    public long getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(long enquiryId) {
        this.enquiryId = enquiryId;
    }

    public SellerEntity getSellerEntity() {
        return sellerEntity;
    }

    public void setSellerEntity(SellerEntity sellerEntity) {
        this.sellerEntity = sellerEntity;
    }

    public PetEntity getPetEntity() {
        return petEntity;
    }

    public void setPetEntity(PetEntity petEntity) {
        this.petEntity = petEntity;
    }

    public String getEnquiryType() {
        return enquiryType;
    }

    public void setEnquiryType(String enquiryType) {
        this.enquiryType = enquiryType;
    }
}
