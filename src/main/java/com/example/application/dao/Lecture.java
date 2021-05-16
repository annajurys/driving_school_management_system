package com.example.application.dao;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Lectures")
public class Lecture {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idLecture;
    private Date date1;
    private Date date2;
    private Date date3;
    private int peopleLimit;
    private int availableSeats;

    public Lecture(Date date1, Date date2, Date date3, int peopleLimit, int availableSeats) {
        this.date1 = date1;
        this.date2 = date2;
        this.date3 = date3;
        this.peopleLimit = peopleLimit;
        this.availableSeats = availableSeats;
    }

    public Lecture() {
    }

    public long getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(long idLecture) {
        this.idLecture = idLecture;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public int getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(int people_limit) {
        this.peopleLimit = people_limit;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
