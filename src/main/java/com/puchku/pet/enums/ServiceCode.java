package com.puchku.pet.enums;

public enum ServiceCode {
    SELLING("S"),
    ADOPTION("A"),
    MATING("M");

    private final String code;

    ServiceCode(String code){
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
