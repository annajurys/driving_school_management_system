package com.example.application.controller;

import com.example.application.dao.Lesson;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.PasswordSHA1;
import com.example.application.dao.User;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class LogInController {

    private PasswordSHA1 passwordSHA1;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private String error;
    private String news;
    private String isInvalid;
    private LessonRepo lessonRepo;
    private List<Lesson> lessonList;

    @Autowired
    public LogInController(PasswordSHA1 passwordSHA1, SessionUtil sessionUtil, UserRepo userRepo, LessonRepo lessonRepo) {
        this.passwordSHA1 = passwordSHA1;
        this.userRepo = userRepo;
        this.sessionUtil = sessionUtil;
        this.lessonRepo = lessonRepo;
    }

    @RequestMapping("/")
    public String get(Model model, HttpSession session) {
        if (null == session.getAttribute("role")) {
            model.addAttribute("checkUser", new User());
            error = "";
            news = "";
            isInvalid = "";
            lessonList = new ArrayList<>();
            lessonList = lessonRepo.findAll();
            for (Lesson lesson : lessonList) {
                if (lesson.getStatus().contains("waiting") && lesson.getDate().getTime() - new Date().getTime() <= 43200000) {
                    lesson.setStatus("cancelled");
                    lessonRepo.save(lesson);
                }
                if (lesson.getStatus().contains("confirmed") && lesson.getDate().before(new Date())) {
                    lesson.setStatus("done");
                    lessonRepo.save(lesson);
                }
            }
            return "login";
        } else if (session.getAttribute("role").equals("learner")) {
            return "redirect:/learner_home";
        } else if (session.getAttribute("role").equals("instructor")) {
            return "redirect:/driving_instructor_home";
        } else {
            return "redirect:/admin_home";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, @RequestParam("roles") String role, HttpSession session) {
        String passwordInSha1 = passwordSHA1.encryptPassword(user.getPassword());

        if (userRepo.existsUserByEmail(user.getEmail())
                && userRepo.findByEmail(user.getEmail()).getPassword().compareTo(passwordInSha1) == 0
                && userRepo.findByEmail(user.getEmail()).getRole().compareTo(role) == 0) {
            sessionUtil.startSession(session, user, role);
            sessionUtil.giveMeSessionStatus(session);
            if (role.equals("learner")) {
                return "redirect:/learner_home";
            } else if (role.equals("instructor")) {
                return "redirect:/driving_instructor_home";
            } else {
                return "redirect:/admin_home";
            }
        } else {
            error = "Uncorrect email, role or password";
            return "redirect:/";
        }
    }

    @ModelAttribute("error")
    public String error() {
        return error;
    }

    @ModelAttribute("news")
    public String news() {
        return news;
    }

}
