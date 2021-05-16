package com.example.application.controller.admin;

import com.example.application.dao.Holiday;
import com.example.application.dao.User;
import com.example.application.dao.repo.HolidayRepo;
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
import java.util.Date;
import java.util.List;

@Controller
public class AdminHolidaysController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private HolidayRepo holidayRepo;
    private List<Holiday> holidayList;
    private SystemMessagesUtil systemMessagesUtil;

    @Autowired
    public AdminHolidaysController(HttpSession session, SessionUtil sessionUtil, HolidayRepo holidayRepo, SystemMessagesUtil systemMessagesUtil) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.holidayRepo = holidayRepo;
        this.systemMessagesUtil = systemMessagesUtil;
    }

    @GetMapping("/admin_holidays")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_holidays")) {
            holidayList = holidayRepo.findAll();
            model.addAttribute("holidayList", holidayList);
            error = "";
            news = "";
            return "admin_holidays";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/change-holiday")
    public String changeUser(@ModelAttribute User user, @RequestParam("id") Long idHoliday) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_holidays")) {
            Holiday chosenHoliday = holidayRepo.findByIdHoliday(idHoliday);
            session.setAttribute("chosenHoliday", chosenHoliday);
            session.setAttribute("chosenHolidayId", chosenHoliday.getIdHoliday());
            return "redirect:/admin_holidays_edit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/set-confirmed")
    public String setConfirmed(@ModelAttribute Holiday holiday, @RequestParam("id") Long idHoliday) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_holidays")) {
            Holiday chosenHoliday = holidayRepo.findByIdHoliday(idHoliday);
            chosenHoliday.setConfirmed(true);
            holidayRepo.save(chosenHoliday);
            systemMessagesUtil.sendMessageFromSystem(chosenHoliday.getIdUser(), "Your holiday from " + chosenHoliday.getDateFrom() + " to " + chosenHoliday.getDateTo() + " is now confirmed in system.");
            return "redirect:/admin_holidays";
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
