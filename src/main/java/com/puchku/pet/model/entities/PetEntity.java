package com.puchku.pet.model.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "pet", schema="public")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private long petId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="user_id")
    @JsonIgnore
    private UserEntity user;

    @Column(name = "breed")
    private String breed;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "pet_type")
    private String petType;

    @Column(name = "age")
    private int age;

    @Column(name = "status")
    private String petStatus;

    @Column(name = "father_breed")
    private String fatherBreed;

    @Column(name = "mother_breed")
    private String motherBreed;

    @Column(name = "crt_ts")
    private Date crtTs;

    @OneToOne(mappedBy="petEntity")
    @JsonIgnore
    private PetServiceEntity service;

    @Column(name = "vaccination_status")
    private Boolean vaccStatus;

    public PetServiceEntity getService() {
        return service;
    }

    public void setService(PetServiceEntity service) {
        this.service = service;
    }

    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userEntity) {
        this.user = userEntity;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPetStatus() {
        return petStatus;
    }

    public void setPetStatus(String petStatus) {
        this.petStatus = petStatus;
    }

    public String getFatherBreed() {
        return fatherBreed;
    }

    public void setFatherBreed(String fatherBreed) {
        this.fatherBreed = fatherBreed;
    }

    public String getMotherBreed() {
        return motherBreed;
    }

    public void setMotherBreed(String motherBreed) {
        this.motherBreed = motherBreed;
    }

    public Date getCrtTs() {
        return crtTs;
    }

    public void setCrtTs(Date crtTs) {
        this.crtTs = crtTs;
    }

    public Boolean getVaccStatus() {
        return vaccStatus;
    }

    public void setVaccStatus(Boolean vaccStatus) {
        this.vaccStatus = vaccStatus;
    }

    @Override
    public String toString() {
        return "PetEntity{" +
                "petId=" + petId +
                ", user=" + user +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", petType='" + petType + '\'' +
                ", age=" + age +
                ", petStatus='" + petStatus + '\'' +
                ", fatherBreed='" + fatherBreed + '\'' +
                ", motherBreed='" + motherBreed + '\'' +
                ", crtTs=" + crtTs +
                ", service=" + service +
                '}';
    }

}
