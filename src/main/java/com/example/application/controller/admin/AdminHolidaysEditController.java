package com.example.application.controller.admin;

import com.example.application.dao.Holiday;
import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.HolidayRepo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminHolidaysEditController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private List<User> userList;
    private List<UserDetail> userDetailList;
    private HolidayRepo holidayRepo;
    private String error;
    private String news;
    private List<Holiday> holidayList;

    @Autowired
    public AdminHolidaysEditController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, UserDetailRepo userDetailRepo, HolidayRepo holidayRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.holidayRepo = holidayRepo;
    }

    @GetMapping("/admin_holidays_edit")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_holidays_edit")) {
            Holiday chosenHoliday = (Holiday) session.getAttribute("chosenHoliday");
            model.addAttribute("editHoliday", chosenHoliday);
            holidayList = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String dateFrom = simpleDateFormat.format(chosenHoliday.getDateFrom());
            model.addAttribute("dateFrom", dateFrom);
            String dateTo = simpleDateFormat.format(chosenHoliday.getDateTo());
            model.addAttribute("dateTo", dateTo);
            error = "";
            news = "";
            return "admin_holidays_edit";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/save-admin-holidays-edit")
    public String saveAdminHolidaysEdit(@RequestParam("dateTo") String dateTo, @RequestParam("dateFrom") String dateFrom, @RequestParam(defaultValue = "false", name = "confirmed") boolean confirmed) throws ParseException {
        Holiday holiday = holidayRepo.findByIdHoliday((Long) session.getAttribute("chosenHolidayId"));
        holidayList = holidayRepo.findByIdUser(holiday.getIdUser());
        Date dateFrom1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(dateFrom);
        Date dateTo1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(dateTo);

        if (dateTo1.before(dateFrom1)) {
            error = "You must pick the end date after the start date!";
            return "redirect:/admin_holidays_edit";
        }
        holidayList.remove(holiday);
        for (Holiday holidayFromList : holidayList) {
            boolean overlay = true;
            if (holidayFromList.getDateTo().before(dateFrom1) || holidayFromList.getDateFrom().after(dateTo1)) {
                overlay = false;
            }
            if (overlay == true) {
                error = "This instructor already have holiday at selected time";
                return "redirect:/admin_holidays_edit";
            }
        }

        holiday.setDateFrom(dateFrom1);
        holiday.setDateTo(dateTo1);
        holiday.setConfirmed(confirmed);
        holidayRepo.save(holiday);
        session.removeAttribute("chosenHolidayId");
        session.removeAttribute("chosenHoliday");
        return "redirect:/admin_holidays";
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
