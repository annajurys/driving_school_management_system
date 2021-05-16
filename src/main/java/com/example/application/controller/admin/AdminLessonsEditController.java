package com.example.application.controller.admin;

import com.example.application.dao.*;
import com.example.application.dao.repo.*;
import com.example.application.utils.SessionUtil;
import com.example.application.utils.SystemMessagesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminLessonsEditController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private List<User> userList;
    private List<UserDetail> userDetailList;
    private LessonRepo lessonRepo;
    private List<Lesson> lessonList;
    private String error;
    private String news;


    @Autowired
    public AdminLessonsEditController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, UserDetailRepo userDetailRepo, LessonRepo lessonRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.lessonRepo = lessonRepo;
    }

    @GetMapping("/admin_lessons_edit")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_lessons_edit")) {
            lessonList = new ArrayList<>();
            lessonList = lessonRepo.findAll();
            Lesson chosenLesson = (Lesson) session.getAttribute("chosenLesson");
            model.addAttribute("editLesson", chosenLesson);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String date1 = simpleDateFormat.format(chosenLesson.getDate());
            model.addAttribute("date", date1);
            error = "";
            news = "";
            return "admin_lessons_edit";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/save-admin-lessons-edit")
    public String saveAdminLessonsEdit(@RequestParam("additional") boolean additional, @RequestParam("date1") String date1, @RequestParam("status") String status) throws ParseException {
        Lesson lesson = lessonRepo.findByIdLesson((Long) session.getAttribute("chosenLessonId"));

        lessonRepo.save(lesson);
        session.removeAttribute("chosenLessonId");
        session.removeAttribute("chosenLesson");
        return "redirect:/admin_lessons";
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
