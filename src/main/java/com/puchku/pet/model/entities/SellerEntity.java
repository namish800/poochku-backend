package com.puchku.pet.model.entities;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "seller", schema="poochku")
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private long sellerId;

    @Column(name = "f_name")
    private String fName;

    @Column(name = "m_name")
    private String mName;

    @Column(name = "l_name")
    private String lName;

    @Column(name = "user_role")
    private List<String> roles;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "crt_ts")
    private OffsetDateTime crtTs;

    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @Column(name = "phone_no")
    private String phoneNo;

    @OneToMany(mappedBy="seller")
    private List<PetEntity> petEntityList;

    public List<PetEntity> getPetEntityList() {
        return petEntityList;
    }

    public void setPetEntityList(List<PetEntity> petEntityList) {
        this.petEntityList = petEntityList;
    }


    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getCrtTs() {
        return crtTs;
    }

    public void setCrtTs(OffsetDateTime crtTs) {
        this.crtTs = crtTs;
    }

    public OffsetDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(OffsetDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + sellerId +
                ", fName='" + fName + '\'' +
                ", mName='" + mName + '\'' +
                ", lName='" + lName + '\'' +
                ", role='" + roles + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", crtTs=" + crtTs +
                ", lastLogin=" + lastLogin +
                ", phoneNo='" + phoneNo +
                '}';
    }
}
