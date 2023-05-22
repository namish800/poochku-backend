package com.puchku.pet.model.entities;

public class Service {
    private String serviceCode;

    private long dogId;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public long getDogId() {
        return dogId;
    }

    public void setDogId(long dogId) {
        this.dogId = dogId;
    }
}