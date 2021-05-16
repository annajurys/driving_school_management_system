package com.example.application.controller.admin;

import com.example.application.dao.Lecture;
import com.example.application.dao.User;
import com.example.application.dao.repo.LectureRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AdminLecturesController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private LectureRepo lectureRepo;
    private List<Lecture> lectureList;
    private String error;
    private String news;

    @Autowired
    public AdminLecturesController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, LectureRepo lectureRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.lectureRepo = lectureRepo;
    }

    @GetMapping("/admin_lectures")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_lectures")) {
            lectureList = lectureRepo.findAll();
            model.addAttribute("lectureList", lectureList);
            model.addAttribute("newLecture", new Lecture());
            error = "";
            news = "";
            return "admin_lectures";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/change-lecture")
    public String changeUser(@ModelAttribute User user, @RequestParam("id") Long idLecture) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_holidays")) {
            Lecture chosenLecture = lectureRepo.findByIdLecture(idLecture);
            session.setAttribute("chosenLecture", chosenLecture);
            session.setAttribute("chosenLectureId", chosenLecture.getIdLecture());
            return "redirect:/admin_lectures_edit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/save-lecture")
    public String addLecture(@ModelAttribute Lecture lecture, @RequestParam("day1") String day1, @RequestParam("day2") String day2, @RequestParam("day3") String day3, @RequestParam("limit") String limit) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(day1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(day2);
            Date date3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(day3);
            if (date1.after(date2) || date2.after(date3) || date1.after(date3)) {
                error = "You must pick the dates after each other!";
                return "redirect:/admin_lectures";
            }
            if (date1.before(new Date())) {
                error = "You must pick the start date after today!";
                return "redirect:/admin_lectures";
            }
            lecture.setDate1(date1);
            lecture.setDate2(date2);
            lecture.setDate3(date3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lecture.setPeopleLimit(Integer.valueOf(limit));
        lecture.setAvailableSeats(Integer.valueOf(limit));
        lectureRepo.save(lecture);
        return "redirect:/admin_lectures";
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
