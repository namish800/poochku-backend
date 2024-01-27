package com.puchku.pet.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "seller_ratings", schema="poochku")
public class SellerRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    @OneToOne
    @JoinColumn(name = "rater_id", referencedColumnName="seller_id")
    @JsonIgnore
    private SellerEntity rater;

    @OneToOne
    @JoinColumn(name = "seller_id", referencedColumnName="seller_id")
    @JsonIgnore
    private SellerEntity seller;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public SellerEntity getRater() {
        return rater;
    }

    public void setRater(SellerEntity rater) {
        this.rater = rater;
    }

    public SellerEntity getSeller() {
        return seller;
    }

    public void setSeller(SellerEntity seller) {
        this.seller = seller;
    }
}
