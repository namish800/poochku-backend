package com.puchku.pet.model.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dog", schema="public")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private long dogId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="user_id")
    @JsonIgnore
    private User user;
    @Column(name = "breed")
    private String breed;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy="dog")
    @JsonIgnore
    private List<DogService> serviceList;

    public List<DogService> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<DogService> serviceList) {
        this.serviceList = serviceList;
    }

    public long getDogId() {
        return dogId;
    }

    public void setDogId(long dogId) {
        this.dogId = dogId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "dogId=" + dogId +
                ", user=" + user +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
