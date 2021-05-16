package com.example.application.controller.learner;

import com.example.application.dao.*;
import com.example.application.dao.repo.LectureRepo;
import com.example.application.dao.repo.RegistrationForLectureRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class LearnerTheoryController {

    private UserDetailRepo userDetailRepo;
    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private LectureRepo lectureRepo;
    private List<Lecture> lectureList;
    private RegistrationForLectureRepo registrationForLectureRepo;
    private String plannedLecture;
    private boolean hasCourse;

    @Autowired
    public LearnerTheoryController(UserDetailRepo userDetailRepo, HttpSession session, SessionUtil sessionUtil, LectureRepo lectureRepo, RegistrationForLectureRepo registrationForLectureRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userDetailRepo = userDetailRepo;
        this.lectureRepo = lectureRepo;
        this.registrationForLectureRepo = registrationForLectureRepo;
    }

    @GetMapping("/learner_theory")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_theory")) {
            if (registrationForLectureRepo.existsByIdLearner((Long) session.getAttribute("id"))) {
                RegistrationForLecture thisRegistration = registrationForLectureRepo.findByIdLearner((Long) session.getAttribute("id"));
                hasCourse = true;
                if (thisRegistration.getLecture().getDate1().after(new Date())) {
                    plannedLecture = "You have planned lecture. First day on " + thisRegistration.getLecture().getDate1();
                } else if (thisRegistration.getLecture().getDate2().after(new Date())) {
                    plannedLecture = "You completed your first day of lectures. Second day of your lecture begins on " + thisRegistration.getLecture().getDate2();
                } else if (thisRegistration.getLecture().getDate3().after(new Date())) {
                    plannedLecture = "You completed your first and second day of lectures. Third day of your lecture begins on " + thisRegistration.getLecture().getDate3();
                } else {
                    plannedLecture = "You have completed your theory course :D";
                }
            } else {
                hasCourse = false;
                plannedLecture = "You do not have planned lecture. Choose from available courses below: ";
            }
            lectureList = lectureRepo.findAllByDate1AfterAndAvailableSeatsGreaterThan(new Date(), 0);
            model.addAttribute("lectureList", lectureList);
            model.addAttribute("hasCourse", hasCourse);
            model.addAttribute("plannedLecture", plannedLecture);
            error = "";
            news = "";
            return "learner_theory";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/registrate-for-lecture")
    public String registrateForLecture(@ModelAttribute RegistrationForLecture registrationForLecture, @RequestParam("id") Long idLecture) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_theory")) {
            Lecture chosenLecture = lectureRepo.findByIdLecture(idLecture);
            registrationForLecture.setIdLearner((Long) session.getAttribute("id"));
            registrationForLecture.setIdLecture(idLecture);
            registrationForLecture.setLecture(lectureRepo.findByIdLecture(idLecture));
            registrationForLecture.setUserDetail(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")));
            registrationForLectureRepo.save(registrationForLecture);
            chosenLecture.setAvailableSeats(chosenLecture.getAvailableSeats() - 1);
            lectureRepo.save(chosenLecture);
            return "redirect:/learner_theory";
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
