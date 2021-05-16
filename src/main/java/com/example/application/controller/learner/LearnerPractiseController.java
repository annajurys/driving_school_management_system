package com.example.application.controller.learner;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class LearnerPractiseController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private SystemMessagesUtil systemMessagesUtil;
    private LessonRepo lessonRepo;
    private UserDetailRepo userDetailRepo;
    private CommentRepo commentRepo;
    private UserRepo userRepo;
    private MessageRepo messageRepo;
    private List<Lesson> lessonList;
    private List<Lesson> lessonPastList;
    private List<Lesson> lessonUpcomingList;
    private List<User> userDrivingInstructorList;
    List<Lesson> lessonDrivingInstructorList;
    private String error;
    private String news;
    private List<Integer> chart;

    @Autowired
    public LearnerPractiseController(HttpSession session, SessionUtil sessionUtil, LessonRepo lessonRepo, CommentRepo commentRepo, UserRepo userRepo, UserDetailRepo userDetailRepo, MessageRepo messageRepo, SystemMessagesUtil systemMessagesUtil) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.lessonRepo = lessonRepo;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.commentRepo = commentRepo;
        this.messageRepo = messageRepo;
        this.systemMessagesUtil = systemMessagesUtil;
    }

    @GetMapping("/learner_practise")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_practise")) {
            lessonList = lessonRepo.findByIdLearnerOrderByDateAsc((Long) session.getAttribute("id"));
            userDrivingInstructorList = userRepo.findByRole("instructor");
            lessonPastList = new ArrayList<>();
            lessonUpcomingList = new ArrayList<>();
            chart = new ArrayList<>();
            int done = 0;
            int planned = 0;
            for (Lesson lesson : lessonList) {
                if (lesson.getDate().after(new Date()) && (lesson.getStatus().contains("waiting") || lesson.getStatus().contains("confirmed"))) {
                    lessonUpcomingList.add(lesson);
                    planned++;
                } else {
                    if (lesson.getStatus().contains("done")) {
                        lessonPastList.add(lesson);
                        done++;
                    }
                }
            }
            chart.add(done);
            chart.add(planned);
            chart.add(30 - done - planned);
            model.addAttribute("userDrivingInstructorList", userDrivingInstructorList);
            model.addAttribute("newLesson", new Lesson());
            model.addAttribute("lessonPastList", lessonPastList);
            model.addAttribute("lessonUpcomingList", lessonUpcomingList);
            model.addAttribute("chart", chart);
            error = "";
            news = "";
            return "learner_practise";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/continue-lesson")
    public String continueLesson(@ModelAttribute Lesson lesson, @RequestParam("instructor1") Long instructor) {
        lesson.setIdDrivingInstructor(instructor);
        session.setAttribute("lessonIdDrivingInstructor", instructor);
        return "redirect:/learner_practise_2";
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
