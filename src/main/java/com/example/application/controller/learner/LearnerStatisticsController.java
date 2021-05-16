package com.example.application.controller.learner;

import com.example.application.dao.Lesson;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import com.example.application.utils.StatisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LearnerStatisticsController {

    private UserRepo userRepo;
    private HttpSession session;
    private SessionUtil sessionUtil;
    private StatisticsUtil statisticsUtil;
    private double averageGrade;
    private double averageGradeAll;
    private String error;
    private String news;
    private List<Double> chart;
    private List<Integer> timeline;
    private LessonRepo lessonRepo;
    private List<Lesson> lessonList;

    @Autowired
    public LearnerStatisticsController(UserRepo userRepo, HttpSession session, SessionUtil sessionUtil, StatisticsUtil statisticsUtil, LessonRepo lessonRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.statisticsUtil = statisticsUtil;
        this.lessonRepo = lessonRepo;
    }

    @GetMapping("/learner_statistics")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_statistics")) {
            averageGrade = statisticsUtil.calculateAverageGrade((Long) session.getAttribute("id"));
            averageGradeAll = statisticsUtil.calculateAverageGrade();
            chart = new ArrayList<>();
            chart.add(averageGrade);
            chart.add(averageGradeAll);
            model.addAttribute("chart", chart);
            lessonList = new ArrayList<>();
            lessonList = lessonRepo.findByIdLearnerOrderByDateAsc((Long) session.getAttribute("id"));
            int a1 = 0;
            int a2 = 0;
            int a3 = 0;
            int a4 = 0;
            int a5 = 0;
            int a6 = 0;
            int a7 = 0;
            int a8 = 0;
            int a9 = 0;
            int a10 = 0;
            int a11 = 0;
            int a12 = 0;
            for (Lesson lesson : lessonList) {
                if (lesson.getStatus().contains("done")) {
                    if (lesson.getDate().getMonth() == 0) {
                        a1++;
                    }
                    if (lesson.getDate().getMonth() == 1) {
                        a2++;
                    }
                    if (lesson.getDate().getMonth() == 8) {
                        a3++;
                    }
                    if (lesson.getDate().getMonth() == 9) {
                        a4++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a5++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a6++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a7++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a8++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a9++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a10++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a11++;
                    }
                    if (lesson.getDate().getMonth() == 10) {
                        a12++;
                    }
                }
            }
            error = "";
            news = "";
            timeline = new ArrayList<>();
            timeline.add(a1);
            timeline.add(a2);
            timeline.add(a3);
            timeline.add(a4);
            timeline.add(a5);
            timeline.add(a6);
            timeline.add(a7);
            timeline.add(a8);
            timeline.add(a9);
            timeline.add(a10);
            timeline.add(a11);
            timeline.add(a12);
            model.addAttribute("timeline", timeline);
            return "learner_statistics";
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
