package com.example.application.dao;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Comments")
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idComment;
    private long idDrivingInstructor;
    private long idLearner;
    private Date date;
    private int grade;
    private String comment;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail_instructor")
    private UserDetail userDetailInstructor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail_learner")
    private UserDetail userDetailLearner;

    public Comment() {
    }

    public Comment(long idDrivingInstructor, long idLearner, Date date, int grade, String comment) {
        this.idDrivingInstructor = idDrivingInstructor;
        this.idLearner = idLearner;
        this.date = date;
        this.grade = grade;
        this.comment = comment;
    }

    public long getIdComment() {
        return idComment;
    }

    public void setIdComment(long idComment) {
        this.idComment = idComment;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public UserDetail getUserDetailInstructor() {
        return userDetailInstructor;
    }

    public void setUserDetailInstructor(UserDetail userDetailInstructor) {
        this.userDetailInstructor = userDetailInstructor;
    }

    public UserDetail getUserDetailLearner() {
        return userDetailLearner;
    }

    public void setUserDetailLearner(UserDetail userDetailLearner) {
        this.userDetailLearner = userDetailLearner;
    }
}
