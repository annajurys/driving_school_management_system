package com.example.application.controller.learner;

import com.example.application.dao.Lesson;
import com.example.application.dao.Message;
import com.example.application.dao.Payment;
import com.example.application.dao.RegistrationForLecture;
import com.example.application.dao.repo.*;
import com.example.application.utils.SessionUtil;
import com.example.application.utils.SystemMessagesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class LearnerHomeController {

    private UserRepo userRepo;
    private LessonRepo lessonRepo;
    private List<Lesson> lessonList;
    private List<Lesson> lessonUpcomingList;
    private List<Lesson> lessonUpcomingTop3List;
    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private List<String> stringList;
    private SystemMessagesUtil systemMessagesUtil;
    private UserDetailRepo userDetailRepo;
    private MessageRepo messageRepo;
    private List<Message> messageList;
    private List<Message> messageUnreadList;
    private PaymentRepo paymentRepo;
    private List<Payment> paymentList;
    private List<Payment> paymentGoodList;
    private LectureRepo lectureRepo;
    private RegistrationForLectureRepo registrationForLectureRepo;
    private List<RegistrationForLecture> registrationForLectures;
    private String lecture;
    private List<Integer> chart;

    @Autowired
    public LearnerHomeController(UserRepo userRepo, UserDetailRepo userDetailRepo, HttpSession session, SessionUtil sessionUtil, LessonRepo lessonRepo, SystemMessagesUtil systemMessagesUtil, MessageRepo messageRepo, PaymentRepo paymentRepo, LectureRepo lectureRepo, RegistrationForLectureRepo registrationForLectureRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.lessonRepo = lessonRepo;
        this.systemMessagesUtil = systemMessagesUtil;
        this.userDetailRepo = userDetailRepo;
        this.messageRepo = messageRepo;
        this.paymentRepo = paymentRepo;
        this.lectureRepo = lectureRepo;
        this.registrationForLectureRepo = registrationForLectureRepo;
    }

    @GetMapping("/learner_home")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_home")) {
            lessonList = lessonRepo.findByIdLearnerOrderByDateAsc((Long) session.getAttribute("id"));
            paymentList = new ArrayList<>();
            paymentGoodList = new ArrayList<>();
            registrationForLectures = new ArrayList<>();
            paymentList = paymentRepo.findByIdLearner((Long) session.getAttribute("id"));
            registrationForLectures = registrationForLectureRepo.findAll();
            lecture = "";
            chart = new ArrayList<>();
            int lectures = 0;
            for (RegistrationForLecture registrationForLecture : registrationForLectures) {
                if (registrationForLecture.getIdLearner() == (Long) session.getAttribute("id")) {
                    if (registrationForLecture.getLecture().getDate3().before(new Date())) {
                        lecture = "You have done all lectures!";
                        lectures = 30;
                    } else {
                        lecture = "You still have lectures.";
                        if (registrationForLecture.getLecture().getDate2().before(new Date()))
                            lectures = 20;
                        else if (registrationForLecture.getLecture().getDate1().before(new Date()))
                            lectures = 10;
                        else
                            lectures = 0;
                    }
                }
            }
            if (lecture == "") {
                lecture = "You need to choose lectures.";
            }

            for (Payment payment : paymentList) {
                if (payment.getEndDate().after(new Date()) && payment.getRealizationDate() == null) {
                    paymentGoodList.add(payment);
                }
            }
            stringList = new ArrayList<>();
            lessonUpcomingList = new ArrayList<>();
            lessonUpcomingTop3List = new ArrayList<>();
            messageList = new ArrayList<>();
            messageList = messageRepo.findAllByIdTo((Long) session.getAttribute("id"));
            messageUnreadList = new ArrayList<>();
            for (Message message : messageList) {
                if (!message.getIsRead()) {
                    messageUnreadList.add(message);
                }
            }

            int lessonsDone = 0;
            for (Lesson lessonListIterator : lessonList) {
                if (lessonListIterator.getStatus().contains("done"))
                    lessonsDone++;

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lessonListIterator.getDate());
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                Date dateEndFromLessonList = calendar.getTime();
                Date dateBeginFromLessonList = lessonListIterator.getDate();
                int yearFromList = calendar.get(Calendar.YEAR);
                int monthFromList = calendar.get(Calendar.MONTH);
                int dayFromList = calendar.get(Calendar.DAY_OF_MONTH);

                calendar.setTime(new Date());
                int yearToday = calendar.get(Calendar.YEAR);
                int monthToday = calendar.get(Calendar.MONTH);
                int dayToday = calendar.get(Calendar.DAY_OF_MONTH);

                if (new Date().after(dateBeginFromLessonList) && new Date().before(dateEndFromLessonList)) {
                    if (lessonListIterator.isAdditional()) {
                        if (lessonListIterator.getStatus() == "confirmed")
                            stringList.add("You have additional lesson with " + lessonListIterator.getUserDetailInstructor().getName() + " " + lessonListIterator.getUserDetailInstructor().getSurname() + " running now! It started at " + lessonListIterator.getDate() + "!");
                    } else {
                        if (lessonListIterator.getStatus() == "confirmed")
                            stringList.add("You have lesson with " + lessonListIterator.getUserDetailInstructor().getName() + " " + lessonListIterator.getUserDetailInstructor().getSurname() + " running now! It started at " + lessonListIterator.getDate() + "!");
                    }
                } else if (yearFromList == yearToday && monthFromList == monthToday && dayFromList == dayToday && new Date().before(dateEndFromLessonList) && (lessonListIterator.getStatus().contains("waiting") || lessonListIterator.getStatus().contains("confirmed"))) {
                    stringList.add("You have lesson with " + lessonListIterator.getUserDetailInstructor().getName() + " " + lessonListIterator.getUserDetailInstructor().getSurname() + " today! It starts at " + lessonListIterator.getDate() + ".");
                }
                if (lessonListIterator.getDate().after(new Date()) && (lessonListIterator.getStatus().contains("confirmed") || lessonListIterator.getStatus().contains("waiting"))) {
                    lessonUpcomingList.add(lessonListIterator);
                }
            }
            chart.add(lectures);
            chart.add(lessonsDone);
            for (int i = 0; i < Math.min(lessonUpcomingList.size(), 3); i++) {
                lessonUpcomingTop3List.add(lessonUpcomingList.get(i));
            }
            model.addAttribute("lessonUpcomingTop3List", lessonUpcomingTop3List);
            model.addAttribute("stringList", stringList);
            model.addAttribute("messageUnreadList", messageUnreadList);
            model.addAttribute("paymentGoodList", paymentGoodList);
            model.addAttribute("lecture", lecture);
            model.addAttribute("chart", chart);
            return "learner_home";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("/send-message-to-confirm")
    public String sendMessageToConfirm(@ModelAttribute Lesson lesson, @RequestParam("id") Long idLesson) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_home")) {
            Lesson chosenLesson = lessonRepo.findByIdLesson(idLesson);
            systemMessagesUtil.sendMessageFromSystem(chosenLesson.getIdDrivingInstructor(), "Learner " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getSurname() + " " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getName() + " requests to confirm lesson on " + chosenLesson.getDate());
            return "redirect:/learner_home";
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
