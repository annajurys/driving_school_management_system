package com.example.application.utils;

import com.example.application.dao.Comment;
import com.example.application.dao.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsUtil {

    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private PaymentRepo paymentRepo;
    private MessageRepo messageRepo;
    private LessonRepo lessonRepo;
    private CommentRepo commentRepo;
    private List<Comment> commentList;

    @Autowired
    public StatisticsUtil(UserRepo userRepo, UserDetailRepo userDetailRepo, PaymentRepo paymentRepo, MessageRepo messageRepo, LessonRepo lessonRepo, CommentRepo commentRepo) {
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.paymentRepo = paymentRepo;
        this.messageRepo = messageRepo;
        this.lessonRepo = lessonRepo;
        this.commentRepo = commentRepo;
    }

    public double calculateAverageGrade(Long id) {
        commentList = commentRepo.findByIdLearner(id);
        double averageGrade = 0;
        double sumOfGrades = 0;
        for (Comment comment : commentList) {
            sumOfGrades += comment.getGrade();
        }
        averageGrade = sumOfGrades / commentList.size();
        return averageGrade;
    }

    public double calculateAverageGrade() {
        commentList = commentRepo.findAll();
        double averageGrade = 0;
        double sumOfGrades = 0;
        for (Comment comment : commentList) {
            sumOfGrades += comment.getGrade();
        }
        averageGrade = sumOfGrades / commentList.size();
        return averageGrade;
    }
}
