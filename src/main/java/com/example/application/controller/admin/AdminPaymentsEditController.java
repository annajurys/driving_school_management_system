package com.example.application.controller.admin;

import com.example.application.dao.Payment;
import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.PaymentRepo;
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
import java.util.Date;
import java.util.List;

@Controller
public class AdminPaymentsEditController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private List<User> userList;
    private List<UserDetail> userDetailList;
    private PaymentRepo paymentRepo;
    private String error;
    private String news;

    @Autowired
    public AdminPaymentsEditController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, UserDetailRepo userDetailRepo, PaymentRepo paymentRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("/admin_payments_edit")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_payments_edit")) {
            Payment chosenPayment = (Payment) session.getAttribute("chosenPayment");
            model.addAttribute("editPayment", chosenPayment);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String endDate = simpleDateFormat.format(chosenPayment.getEndDate());
            model.addAttribute("endDate", endDate);
            String realizeDate;
            if (chosenPayment.getRealizationDate() != null) {
                realizeDate = simpleDateFormat.format(chosenPayment.getRealizationDate());
            } else {
                realizeDate = null;
            }
            model.addAttribute("realizeDate", realizeDate);
            error = "";
            news = "";
            return "admin_payments_edit";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/save-admin-payments-edit")
    public String saveAdminPaymentEdit(@RequestParam("endDate") String endDate, @RequestParam("price") double price, @RequestParam("realizeDate") String realizeDate) {
        Payment payment1 = paymentRepo.findByIdPayment((Long) session.getAttribute("chosenPaymentId"));
        session.removeAttribute("chosenPayment");
        session.removeAttribute("chosenPaymentId");
        Date endDate1;
        try {
            endDate1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(endDate);
            if (endDate1.before(new Date())) {
                error = "You must pick the start date after today!";
                return "redirect:/admin_payments_edit";
            }
            payment1.setEndDate(endDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (realizeDate != "") {
            Date realizeDate1;
            try {
                realizeDate1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(realizeDate);
                payment1.setRealizationDate(realizeDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (price > 0) {
            payment1.setPrice(price);
        } else {
            error = "You must pick the price greater than 0!";
            return "redirect:/admin_payments_edit";
        }
        paymentRepo.save(payment1);
        return "redirect:/admin_payments";
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
