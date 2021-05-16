package com.example.application.controller.admin;

import com.example.application.dao.Lecture;
import com.example.application.dao.RegistrationForLecture;
import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.LectureRepo;
import com.example.application.dao.repo.RegistrationForLectureRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
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
import java.util.Date;
import java.util.List;

@Controller
public class AdminLecturesEditController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private List<User> userList;
    private List<UserDetail> userDetailList;
    private LectureRepo lectureRepo;
    private String error;
    private String news;
    private SystemMessagesUtil systemMessagesUtil;
    private RegistrationForLectureRepo registrationForLectureRepo;
    private List<RegistrationForLecture> registrationForLectureList;


    @Autowired
    public AdminLecturesEditController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, UserDetailRepo userDetailRepo, LectureRepo lectureRepo, SystemMessagesUtil systemMessagesUtil, RegistrationForLectureRepo registrationForLectureRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.lectureRepo = lectureRepo;
        this.systemMessagesUtil = systemMessagesUtil;
        this.registrationForLectureRepo = registrationForLectureRepo;
    }

    @GetMapping("/admin_lectures_edit")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_lectures_edit")) {
            Lecture chosenLecture = (Lecture) session.getAttribute("chosenLecture");
            model.addAttribute("editLecture", chosenLecture);
            model.addAttribute("data", "2020-02-01T03:02");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String date1 = simpleDateFormat.format(chosenLecture.getDate1());
            model.addAttribute("date1", date1);
            String date2 = simpleDateFormat.format(chosenLecture.getDate2());
            model.addAttribute("date2", date2);
            String date3 = simpleDateFormat.format(chosenLecture.getDate3());
            model.addAttribute("date3", date3);
            model.addAttribute("peopleLimit", chosenLecture.getPeopleLimit());
            error = "";
            news = "";
            return "admin_lectures_edit";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/save-admin-lectures-edit")
    public String saveAdminLecturesEdit(@RequestParam("peopleLimit") int peopleLimit, @RequestParam("date1") String date1, @RequestParam("date2") String date2, @RequestParam("date3") String date3) throws ParseException {
        Lecture lecture1 = lectureRepo.findByIdLecture((Long) session.getAttribute("chosenLectureId"));
        Date date11 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(date1);
        lecture1.setDate1(date11);
        Date date22 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(date2);
        lecture1.setDate2(date22);
        Date date33 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(date3);
        lecture1.setDate3(date33);
        if (peopleLimit > (lecture1.getPeopleLimit() - lecture1.getAvailableSeats())) {
            lecture1.setPeopleLimit(peopleLimit);
        } else {
            error = "You can not set people limit smaller than number of people who are registrated!";
            return "redirect:/admin_lectures_edit";
        }
        if (peopleLimit >= (lecture1.getAvailableSeats())) {
            lecture1.setPeopleLimit(peopleLimit);
        } else {
            error = "You can not set people limit smaller than available seats!";
            return "redirect:/admin_lectures_edit";
        }
        if (date11.after(date22) || date22.after(date33) || date11.after(date33) || date11.equals(date22) || date22.equals(date33) || date11.equals(date33)) {
            error = "You must pick the dates after each other!";
            return "redirect:/admin_lectures_edit";
        }
        lectureRepo.save(lecture1);
        session.removeAttribute("chosenLectureId");
        session.removeAttribute("chosenLecture");
        registrationForLectureList = registrationForLectureRepo.findByIdLecture(lecture1.getIdLecture());
        for (RegistrationForLecture registrationForLecture : registrationForLectureList) {
            systemMessagesUtil.sendMessageFromSystem(registrationForLecture.getIdLearner(), "Admin has just changed your lecture. Check dates on the Theory tab.");
        }
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
