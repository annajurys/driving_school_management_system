package com.example.application.dao;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Messages")
public class Message {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idMessage;
    private long idFrom;
    private long idTo;
    private Date date;
    private String messageContent;
    private boolean isRead;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail_to")
    private UserDetail userDetailTo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_detail_from")
    private UserDetail userDetailFrom;

    public Message() {
    }

    public Message(long idFrom, long idTo, Date date, String messageContent, boolean isRead) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.date = date;
        this.messageContent = messageContent;
        this.isRead = isRead;
    }

    public long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(long idMessage) {
        this.idMessage = idMessage;
    }

    public long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(long idDrivingInstructor) {
        this.idFrom = idDrivingInstructor;
    }

    public long getIdTo() {
        return idTo;
    }

    public void setIdTo(long idLearner) {
        this.idTo = idLearner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String message) {
        this.messageContent = message;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public UserDetail getUserDetailTo() {
        return userDetailTo;
    }

    public void setUserDetailTo(UserDetail userDetailTo) {
        this.userDetailTo = userDetailTo;
    }

    public UserDetail getUserDetailFrom() {
        return userDetailFrom;
    }

    public void setUserDetailFrom(UserDetail userDetailFrom) {
        this.userDetailFrom = userDetailFrom;
    }

}
