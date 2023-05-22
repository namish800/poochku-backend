package com.puchku.pet.model.entities;
import com.puchku.pet.model.entities.Dog;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user", schema="public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "f_name")
    private String fName;

    @Column(name = "m_name")
    private String mName;

    @Column(name = "l_name")
    private String lName;

    @Column(name = "user_role")
    private String role;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "crt_ts")
    private Date crtTs;

    @Column(name = "last_login")
    private Date lastLogin;

    @OneToMany(mappedBy="user")
    private List<Dog> dogList;

    public List<Dog> getDogList() {
        return dogList;
    }

    public void setDogList(List<Dog> dogList) {
        this.dogList = dogList;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Date getCrtTs() {
        return crtTs;
    }

    public void setCrtTs(Date crtTs) {
        this.crtTs = crtTs;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fName='" + fName + '\'' +
                ", mName='" + mName + '\'' +
                ", lName='" + lName + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", crtTs=" + crtTs +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
