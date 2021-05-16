package com.example.application.dao;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Payments")
public class Payment {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idPayment;
    private long idLearner;
    private double price;
    private String reason;
    private Date endDate;
    private Date realizationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail")
    private UserDetail userDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    public Payment() {
    }

    public Payment(long idLearner, double price, String reason, Date endDate, Date realizationDate) {
        this.idLearner = idLearner;
        this.price = price;
        this.reason = reason;
        this.endDate = endDate;
        this.realizationDate = realizationDate;
    }

    public long getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(long idPayment) {
        this.idPayment = idPayment;
    }

    public long getIdLearner() {
        return idLearner;
    }

    public void setIdLearner(long id_learner) {
        this.idLearner = id_learner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date end_date) {
        this.endDate = end_date;
    }

    public Date getRealizationDate() {
        return realizationDate;
    }

    public void setRealizationDate(Date realize_date) {
        this.realizationDate = realize_date;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
