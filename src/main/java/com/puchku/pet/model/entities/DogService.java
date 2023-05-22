package com.puchku.pet.model.entities;
import com.puchku.pet.model.entities.Dog;
import jakarta.persistence.*;

@Entity
@Table(name = "dog_service", schema="public")
public class DogService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_service_id")
    private long dogServiceId;

    @ManyToOne
    @JoinColumn(name = "dog_id", referencedColumnName="dog_id")
    private Dog dog;

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

    public long getDogServiceId() {
        return dogServiceId;
    }

    public void setDogServiceId(long dogServiceId) {
        this.dogServiceId = dogServiceId;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

}
