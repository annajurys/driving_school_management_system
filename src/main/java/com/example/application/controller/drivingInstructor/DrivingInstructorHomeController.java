package com.example.application.controller.drivingInstructor;

import com.example.application.dao.Holiday;
import com.example.application.dao.Lesson;
import com.example.application.dao.Message;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.MessageRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.utils.SessionUtil;
import com.example.application.utils.SystemMessagesUtil;
import com.example.application.utils.SystemPaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class DrivingInstructorHomeController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private LessonRepo lessonRepo;
    private List<Lesson> lessonList;
    private List<Lesson> lessonUpcomingList;
    private List<Lesson> lessonUpcomingTop3List;
    private UserDetailRepo userDetailRepo;
    private List<String> stringList;
    private SystemMessagesUtil systemMessagesUtil;
    private SystemPaymentUtil systemPaymentUtil;
    private MessageRepo messageRepo;
    private List<Message> messageList;
    private List<Message> messageUnreadList;

    private String error;
    private String news;

    @Autowired
    public DrivingInstructorHomeController(HttpSession session, SessionUtil sessionUtil, LessonRepo lessonRepo, UserDetailRepo userDetailRepo, SystemMessagesUtil systemMessagesUtil, SystemPaymentUtil systemPaymentUtil, MessageRepo messageRepo) {
        this.sessionUtil = sessionUtil;
        this.session = session;
        this.lessonRepo = lessonRepo;
        this.userDetailRepo = userDetailRepo;
        this.systemMessagesUtil = systemMessagesUtil;
        this.systemPaymentUtil = systemPaymentUtil;
        this.messageRepo = messageRepo;
    }

    @GetMapping("/driving_instructor_home")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_home")) {
            lessonList = lessonRepo.findByIdDrivingInstructorOrderByDateAsc((Long) session.getAttribute("id"));
            lessonUpcomingList = new ArrayList<>();
            stringList = new ArrayList<>();
            lessonUpcomingTop3List = new ArrayList<>();
            messageList = new ArrayList<>();
            messageList = messageRepo.findAllByIdTo((Long) session.getAttribute("id"));
            messageUnreadList = new ArrayList<>();
            for (Message message : messageList) {
                if (!message.getIsRead()) {
                    messageUnreadList.add(message);
                }
            }
            for (Lesson lessonListIterator : lessonList) {
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
                            stringList.add("You have additional lesson with " + lessonListIterator.getUserDetailLearner().getName() + " " + lessonListIterator.getUserDetailLearner().getSurname() + " running now! It started at " + lessonListIterator.getDate() + "!");
                    } else {
                        if (lessonListIterator.getStatus() == "confirmed")
                            stringList.add("You have lesson with " + lessonListIterator.getUserDetailLearner().getName() + " " + lessonListIterator.getUserDetailLearner().getSurname() + " running now! It started at " + lessonListIterator.getDate() + "!");
                    }
                } else if (yearFromList == yearToday && monthFromList == monthToday && dayFromList == dayToday && new Date().before(dateEndFromLessonList) && lessonListIterator.getStatus().contains("confirmed")) {
                    stringList.add("You have lesson with " + lessonListIterator.getUserDetailLearner().getName() + " " + lessonListIterator.getUserDetailLearner().getSurname() + " today! It starts at " + lessonListIterator.getDate() + ".");
                }
                if (lessonListIterator.getDate().after(new Date()) && (lessonListIterator.getStatus().contains("confirmed") || lessonListIterator.getStatus().contains("waiting"))) {
                    lessonUpcomingList.add(lessonListIterator);
                }
            }
            for (int i = 0; i < Math.min(lessonUpcomingList.size(), 3); i++) {
                lessonUpcomingTop3List.add(lessonUpcomingList.get(i));
            }
            model.addAttribute("lessonUpcomingTop3List", lessonUpcomingTop3List);
            List<Lesson> tableList = new ArrayList<>();
            for (Lesson lesson : lessonUpcomingTop3List) {
                tableList.add(lesson);
            }
            model.addAttribute("tableList", tableList);
            model.addAttribute("lessonList", lessonList);
            model.addAttribute("stringList", stringList);
            model.addAttribute("messageUnreadList", messageUnreadList);
            error = "";
            news = "";
            return "driving_instructor_home";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/set-confirmed-lesson-from-home")
    public String setConfirmedLessonFromHome(@ModelAttribute Lesson lesson, @RequestParam("id") Long idLesson) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_home")) {
            Lesson chosenLesson = lessonRepo.findByIdLesson(idLesson);
            chosenLesson.setStatus("confirmed");
            lessonRepo.save(chosenLesson);
            systemMessagesUtil.sendMessageFromSystem(chosenLesson.getIdLearner(), "Instructor " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getSurname() + " " + userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")).getName() + " has just confirmed you lesson on " + chosenLesson.getDate());

            if (chosenLesson.isAdditional()) {
                systemPaymentUtil.addPaymentFromSystem(chosenLesson.getIdLearner(), 50, "Additional lesson");
                systemMessagesUtil.sendMessageFromSystem(chosenLesson.getIdLearner(), "You have new payment - instructor has just confirmed your additional lesson on " + chosenLesson.getDate());
            }

            return "redirect:/driving_instructor_home";
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
