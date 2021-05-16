package com.example.application.dao;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Holidays")
public class Holiday {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idHoliday;
    private long idUser;
    private Date dateFrom;
    private Date dateTo;
    private boolean isConfirmed;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail")
    private UserDetail userDetail;

    public Holiday() {
    }

    public Holiday(long idUser, Date dateFrom, Date dateTo, boolean isConfirmed) {
        this.idUser = idUser;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.isConfirmed = isConfirmed;
    }

    public long getIdHoliday() {
        return idHoliday;
    }

    public void setIdHoliday(long idHoliday) {
        this.idHoliday = idHoliday;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date date_from) {
        this.dateFrom = date_from;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date date_to) {
        this.dateTo = date_to;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }
}
