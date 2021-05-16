package com.example.application.dao;

import javax.persistence.*;

@Entity
@Table(name = "UserDetails")
public class UserDetail {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idUserDetail;
    private String name;
    private String surname;
    private String address;
    private String telephone;
    private String avatar;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_detail")
    private User user;

    public UserDetail() {
    }

    public UserDetail(String name, String surname, String address, String telephone, String avatar) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.telephone = telephone;
        this.avatar = avatar;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getIdUserDetail() {
        return idUserDetail;
    }

    public void setIdUserDetail(long idUserDetail) {
        this.idUserDetail = idUserDetail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}

