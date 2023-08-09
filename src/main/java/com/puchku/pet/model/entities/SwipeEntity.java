package com.puchku.pet.model.entities;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name = "Swipe", schema="poochku")
public class SwipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "swipe_id")
    private Long swipeId;

    @ManyToOne
    @JoinColumn(name = "swiper_id", referencedColumnName = "pet_id")
    private PetEntity swiper;

    @ManyToOne
    @JoinColumn(name = "target_id", referencedColumnName = "pet_id")
    private PetEntity target;

    @Column(name = "direction")
    private String direction;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    private String status;

    // Default constructor
    public SwipeEntity() {
    }

    // Parameterized constructor
    public SwipeEntity(PetEntity swiper, PetEntity target, String direction, Timestamp createdAt, String status) {
        this.swiper = swiper;
        this.target = target;
        this.direction = direction;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getSwipeId() {
        return swipeId;
    }

    public void setSwipeId(Long swipeId) {
        this.swipeId = swipeId;
    }

    public PetEntity getSwiper() {
        return swiper;
    }

    public void setSwiper(PetEntity swiper) {
        this.swiper = swiper;
    }

    public PetEntity getTarget() {
        return target;
    }

    public void setTarget(PetEntity target) {
        this.target = target;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
