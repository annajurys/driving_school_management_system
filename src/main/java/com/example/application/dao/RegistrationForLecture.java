package com.example.application.dao;


import javax.persistence.*;

@Entity
@Table(name = "RegistrationsForLectures")
public class RegistrationForLecture {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idRegistrationForLecture;
    private long idLearner;
    private long idLecture;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail")
    private UserDetail userDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_lecture_details")
    private Lecture lecture;

    public RegistrationForLecture() {
    }

    public RegistrationForLecture(long idLearner, long idLecture) {
        this.idLearner = idLearner;
        this.idLecture = idLecture;
    }

    public long getIdRegistrationForLecture() {
        return idRegistrationForLecture;
    }

    public void setIdRegistrationForLecture(long idRegistrationForLecture) {
        this.idRegistrationForLecture = idRegistrationForLecture;
    }

    public long getIdLearner() {
        return idLearner;
    }

    public void setIdLearner(long idLearner) {
        this.idLearner = idLearner;
    }

    public long getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(long idLecture) {
        this.idLecture = idLecture;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
