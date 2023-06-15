package com.puchku.pet.model.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "pet_service", schema="public")
public class PetServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_service_id")
    private long petServiceId;

    @OneToOne
    @JoinColumn(name = "pet_id", referencedColumnName="pet_id")
    private PetEntity petEntity;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_code")
    private String serviceCode;


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public long getPetServiceId() {
        return petServiceId;
    }

    public void setPetServiceId(long petServiceId) {
        this.petServiceId = petServiceId;
    }

    public PetEntity getPetEntity() {
        return petEntity;
    }

    public void setPetEntity(PetEntity petEntity) {
        this.petEntity = petEntity;
    }

}
