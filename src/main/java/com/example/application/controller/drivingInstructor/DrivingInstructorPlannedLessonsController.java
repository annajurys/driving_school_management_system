package com.example.application.controller.drivingInstructor;

import com.example.application.dao.Comment;
import com.example.application.dao.Lesson;
import com.example.application.dao.Message;
import com.example.application.dao.Payment;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.MessageRepo;
import com.example.application.dao.repo.PaymentRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.utils.SessionUtil;
import com.example.application.utils.SystemMessagesUtil;
import com.example.application.utils.SystemPaymentUtil;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DrivingInstructorPlannedLessonsController {

    private LessonRepo lessonRepo;
    private UserDetailRepo userDetailRepo;
    private MessageRepo messageRepo;
    private SystemPaymentUtil systemPaymentUtil;
    private SystemMessagesUtil systemMessagesUtil;
    private List<Lesson> lessonList;
    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;

    @Autowired
    public DrivingInstructorPlannedLessonsController(HttpSession session, SessionUtil sessionUtil, LessonRepo lessonRepo, UserDetailRepo userDetailRepo, MessageRepo messageRepo, SystemPaymentUtil systemPaymentUtil, SystemMessagesUtil systemMessagesUtil) {
        this.sessionUtil = sessionUtil;
        this.session = session;
        this.lessonRepo = lessonRepo;
        this.userDetailRepo = userDetailRepo;
        this.messageRepo = messageRepo;
        this.systemPaymentUtil = systemPaymentUtil;
        this.systemMessagesUtil = systemMessagesUtil;
    }

    @GetMapping("/driving_instructor_planned_lessons")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_planned_lessons")) {
            lessonList = lessonRepo.findByDateAfterAndIdDrivingInstructorOrderByDateAsc(new Date(), (Long) session.getAttribute("id"));
            model.addAttribute("lessonList", lessonList);
            error = "";
            news = "";
            return "driving_instructor_planned_lessons";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/set-confirmed-lesson-from-planned-lessons")
    public String setConfirmedLessonFromPlannedLessons(@ModelAttribute Lesson lesson, @RequestParam("id") Long idLesson) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_planned_lessons")) {
            Lesson chosenLesson = lessonRepo.findByIdLesson(idLesson);
            chosenLesson.setStatus("confirmed");
            lessonRepo.save(chosenLesson);

            systemMessagesUtil.sendMessageFromSystem(chosenLesson.getIdLearner(), "Instructor " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getSurname() + " " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getName() + " has just confirmed you lesson on " + chosenLesson.getDate());

            if (chosenLesson.isAdditional()) {
                systemPaymentUtil.addPaymentFromSystem(chosenLesson.getIdLearner(), 50, "Additional lesson");
                systemMessagesUtil.sendMessageFromSystem(chosenLesson.getIdLearner(), "You have new payment - instructor has just confirmed your additional lesson on " + chosenLesson.getDate());
            }
            return "redirect:/driving_instructor_planned_lessons";
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
