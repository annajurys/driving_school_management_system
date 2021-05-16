package com.example.application.dao;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Lessons")
public class Lesson {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idLesson;
    private long idDrivingInstructor;
    private long idLearner;
    private Date date;
    private String status;
    private boolean isAdditional;


    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private Comment comment;

    @ManyToOne(cascade = CascadeType.ALL)
    //tworzy w bazie kolumnę gdzie trzeba wpisać to co się chce z drugiej tabeli czyli u mnie id_dri_ins
    @JoinColumn(name = "id_user_detail_instructor")
    private UserDetail userDetailInstructor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail_learner")
    private UserDetail userDetailLearner;

    public Lesson() {
    }

    public Lesson(long idDrivingInstructor, long idLearner, Date date, String status, boolean isAdditional) {
        this.idDrivingInstructor = idDrivingInstructor;
        this.idLearner = idLearner;
        this.date = date;
        this.status = status;
        this.isAdditional = isAdditional;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public UserDetail getUserDetailInstructor() {
        return userDetailInstructor;
    }

    public void setUserDetailInstructor(UserDetail userDetail) {
        this.userDetailInstructor = userDetail;
    }

    public long getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(long idLesson) {
        this.idLesson = idLesson;
    }

    public long getIdDrivingInstructor() {
        return idDrivingInstructor;
    }

    public void setIdDrivingInstructor(long idDrivingInstructor) {
        this.idDrivingInstructor = idDrivingInstructor;
    }

    public long getIdLearner() {
        return idLearner;
    }

    public void setIdLearner(long idLearner) {
        this.idLearner = idLearner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAdditional() {
        return isAdditional;
    }

    public void setAdditional(boolean additional) {
        isAdditional = additional;
    }

    public UserDetail getUserDetailLearner() {
        return userDetailLearner;
    }

    public void setUserDetailLearner(UserDetail userDetailLearner) {
        this.userDetailLearner = userDetailLearner;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
