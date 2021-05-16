package com.example.application.controller.learner;

import com.example.application.dao.Holiday;
import com.example.application.dao.Lesson;
import com.example.application.dao.Message;
import com.example.application.dao.User;
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
public class LearnerPractise2Controller {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private SystemMessagesUtil systemMessagesUtil;
    private LessonRepo lessonRepo;
    private UserDetailRepo userDetailRepo;
    private CommentRepo commentRepo;
    private HolidayRepo holidayRepo;
    private UserRepo userRepo;
    private MessageRepo messageRepo;
    private List<Lesson> lessonList;
    private List<Lesson> lessonPastList;
    private List<Lesson> lessonUpcomingList;
    private List<User> userDrivingInstructorList;
    List<Lesson> lessonDrivingInstructorList;
    private String error;
    private String news;

    @Autowired
    public LearnerPractise2Controller(HttpSession session, SessionUtil sessionUtil, LessonRepo lessonRepo, HolidayRepo holidayRepo, CommentRepo commentRepo, UserRepo userRepo, UserDetailRepo userDetailRepo, MessageRepo messageRepo, SystemMessagesUtil systemMessagesUtil) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.lessonRepo = lessonRepo;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.commentRepo = commentRepo;
        this.messageRepo = messageRepo;
        this.systemMessagesUtil = systemMessagesUtil;
        this.holidayRepo = holidayRepo;
    }

    @GetMapping("/learner_practise_2")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_practise_2")) {
            lessonList = lessonRepo.findByIdLearnerOrderByDateAsc((Long) session.getAttribute("id"));
            userDrivingInstructorList = userRepo.findByRole("instructor");
            lessonPastList = new ArrayList<>();
            lessonUpcomingList = new ArrayList<>();
            for (Lesson lesson : lessonList) {
                if (lesson.getDate().after(new Date())) {
                    lessonUpcomingList.add(lesson);
                } else {
                    lessonPastList.add(lesson);
                }
            }
            model.addAttribute("userDrivingInstructorList", userDrivingInstructorList);
            model.addAttribute("newLesson", new Lesson());
            model.addAttribute("lessonPastList", lessonPastList);
            model.addAttribute("lessonUpcomingList", lessonUpcomingList);
            error = "";
            news = "";
            return "learner_practise_2";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/save-lesson")
    public String addLesson(@ModelAttribute Lesson lesson, @ModelAttribute Message message, @RequestParam("date12") String date, @RequestParam(defaultValue = "false", name = "additional1") Boolean additional) {
        Date dateBeginLessonFromForm = new Date();
        try {
            dateBeginLessonFromForm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(date);
            if (dateBeginLessonFromForm.before(new Date())) {
                error = "You must pick the date after today!";
                return "redirect:/learner_practise_2";
            }
            lesson.setDate(dateBeginLessonFromForm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lesson.setStatus("waiting");
        lesson.setAdditional(additional);
        lesson.setIdDrivingInstructor((Long) session.getAttribute("lessonIdDrivingInstructor"));
        lesson.setIdLearner((Long) session.getAttribute("id"));
        lesson.setUserDetailInstructor(userDetailRepo.findByUser(userRepo.findByIdUser((Long) session.getAttribute("lessonIdDrivingInstructor"))));
        lesson.setUserDetailLearner(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBeginLessonFromForm);
        int minutes = dateBeginLessonFromForm.getMinutes();
        if (minutes != 0 && minutes != 30) {
            error = "You must pick the date with full or half hour";
            return "redirect:/learner_practise_2";
        }
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date dateEndLessonFromForm = calendar.getTime();

        List<Holiday> holidayList = holidayRepo.findByIdUserAndDateToAfter((Long) session.getAttribute("lessonIdDrivingInstructor"), new Date());
        for (Holiday holiday : holidayList) {
            if ((dateBeginLessonFromForm.before(holiday.getDateFrom()) && dateEndLessonFromForm.after(holiday.getDateFrom())) || (dateBeginLessonFromForm.before(holiday.getDateTo()) && dateEndLessonFromForm.after(holiday.getDateTo())) || (dateBeginLessonFromForm.after(holiday.getDateFrom()) && dateEndLessonFromForm.before(holiday.getDateTo()))) {
                error = "Chosen instructor has holiday on that day";
                return "redirect:/learner_practise_2";
            }
        }

        for (Lesson lessonListIterator : lessonList) {
            calendar = Calendar.getInstance();
            calendar.setTime(lessonListIterator.getDate());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date dateEndFromLessonList = calendar.getTime();
            if ((dateEndFromLessonList.after(dateBeginLessonFromForm) && lessonListIterator.getDate().before(dateBeginLessonFromForm)) || (lessonListIterator.getDate().before(dateEndLessonFromForm) && dateEndFromLessonList.after(dateEndLessonFromForm))) {
                error = "You already have lesson at selected time";
                return "redirect:/learner_practise_2";
            }
        }
        lessonDrivingInstructorList = lessonRepo.findByIdDrivingInstructorOrderByDateAsc((Long) session.getAttribute("lessonIdDrivingInstructor"));
        for (Lesson lessonDrivingInstructorListIterator : lessonDrivingInstructorList) {
            calendar = Calendar.getInstance();
            calendar.setTime(lessonDrivingInstructorListIterator.getDate());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date dateEndFromLessonList = calendar.getTime();
            if ((dateEndFromLessonList.after(dateBeginLessonFromForm) && lessonDrivingInstructorListIterator.getDate().before(dateBeginLessonFromForm)) || (lessonDrivingInstructorListIterator.getDate().before(dateEndLessonFromForm) && dateEndFromLessonList.after(dateEndLessonFromForm))) {
                error = "You already have lesson at selected time";
                return "redirect:/learner_practise_2";
            }
        }
        lessonRepo.save(lesson);
        systemMessagesUtil.sendMessageFromSystem((Long) session.getAttribute("lessonIdDrivingInstructor"), "Request for a lesson: From " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getSurname() + " " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getName() + ", Date:  " + dateBeginLessonFromForm.toString());
        message.setMessageContent("Request for a lesson: From " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getSurname() + " " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getName() + ", Date:  " + dateBeginLessonFromForm.toString());
        session.removeAttribute("lessonIdDrivingInstructor");
        return "redirect:/learner_practise";
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
