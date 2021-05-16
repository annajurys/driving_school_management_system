package com.example.application.controller.drivingInstructor;

import com.example.application.dao.Holiday;
import com.example.application.dao.repo.HolidayRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.utils.SessionUtil;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DrivingInstructorHolidayController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private HolidayRepo holidayRepo;
    private UserDetailRepo userDetailRepo;
    private List<Holiday> holidayList;
    private String error;
    private String news;

    @Autowired
    public DrivingInstructorHolidayController(HttpSession session, SessionUtil sessionUtil, HolidayRepo holidayRepo, UserDetailRepo userDetailRepo) {
        this.sessionUtil = sessionUtil;
        this.session = session;
        this.holidayRepo = holidayRepo;
        this.userDetailRepo = userDetailRepo;
    }

    @GetMapping("/driving_instructor_holiday")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_holiday")) {
            holidayList = holidayRepo.findByIdUser((Long) session.getAttribute("id"));
            model.addAttribute("holidayList", holidayList);
            model.addAttribute("newHoliday", new Holiday());
            error = "";
            news = "";
            return "driving_instructor_holiday";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/save-holiday")
    public String addHoliday(@ModelAttribute Holiday holiday, @RequestParam("dateFrom12") String dateFrom, @RequestParam("dateTo12") String dateTo, HttpSession session) {
        try {
            Date dateFrom1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
            Date dateTo1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
            if (dateTo1.before(dateFrom1)) {
                error = "You must pick the end date after the start date!";
                return "redirect:/driving_instructor_holiday";
            }
            if (dateFrom1.before(new Date())) {
                error = "You must pick the start date after today!";
                return "redirect:/driving_instructor_holiday";
            }
            for (Holiday holiday1 : holidayList) {
                boolean overlay = true;
                if (holiday1.getDateTo().before(dateFrom1) || holiday1.getDateFrom().after(dateTo1)) {
                    overlay = false;
                }
                if (overlay == true) {
                    error = "You already have holiday at selected time";
                    return "redirect:/driving_instructor_holiday";
                }
            }
            holiday.setDateFrom(dateFrom1);
            holiday.setDateTo(dateTo1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holiday.setIdUser((Long) session.getAttribute("id"));
        holiday.setConfirmed(false);
        holiday.setUserDetail(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")));
        holidayRepo.save(holiday);
        news = "Request sent";
        return "redirect:/driving_instructor_holiday";
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
