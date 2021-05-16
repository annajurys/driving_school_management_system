package com.example.application;

import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    HolidayRepo holidayRepo;
    @Autowired
    LectureRepo lectureRepo;
    @Autowired
    LessonRepo lessonRepo;
    @Autowired
    MessageRepo messageRepo;
    @Autowired
    PaymentRepo paymentRepo;
    @Autowired
    RegistrationForLectureRepo registrationForLectureRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserDetailRepo userDetailRepo;

    @PostConstruct
    public void init() {
        this.userRepo.save(new User("anna.j@gmail.com", "", "learner"));
        this.userRepo.save(new User("kuba.z@gmail.com", "", "learner"));
        this.userRepo.save(new User("klaudia.k@gmail.com", "", "learner"));
        this.userRepo.save(new User("kasia.k@gmail.com", "", "learner"));
        this.userRepo.save(new User("zofia.z@gmail.com", "", "admin"));
        this.userRepo.save(new User("joanna.a@gmail.com", "", "instructor"));
        this.userRepo.save(new User("tadeusz.b@gmail.com", "", "instructor"));
        this.userRepo.save(new User("radosław.c@gmail.com", "", "instructor"));
        //this.userDetailRepo.save(new UserDetail("anna", "j", "j", 111222333, ));



    }


}
