package com.example.application.controller.admin;

import com.example.application.dao.Lesson;
import com.example.application.dao.Payment;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.PaymentRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminLessonsController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private LessonRepo lessonRepo;
    List<Lesson> lessonList;

    @Autowired
    public AdminLessonsController(HttpSession session, SessionUtil sessionUtil, LessonRepo lessonRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.lessonRepo = lessonRepo;
    }

    @GetMapping("/admin_lessons")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_lessons")) {
            lessonList = lessonRepo.findAll();
            model.addAttribute("lessonList", lessonList);
            error = "";
            news = "";
            return "admin_lessons";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/change-lesson")
    public String changeLesson(@ModelAttribute Lesson lesson, @RequestParam("id") Long idLesson) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_lessons")) {
            Lesson chosenLesson = lessonRepo.findByIdLesson(idLesson);
            session.setAttribute("chosenLesson", chosenLesson);
            session.setAttribute("chosenLessonId", chosenLesson.getIdLesson());
            return "redirect:/admin_lessons_edit";
        } else {
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
