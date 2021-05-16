package com.example.application.controller.admin;

import com.example.application.dao.Payment;
import com.example.application.dao.repo.PaymentRepo;
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
public class AdminPaymentsController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private PaymentRepo paymentRepo;
    List<Payment> paymentList;
    private SystemMessagesUtil systemMessagesUtil;

    @Autowired
    public AdminPaymentsController(HttpSession session, SessionUtil sessionUtil, PaymentRepo paymentRepo, SystemMessagesUtil systemMessagesUtil) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.paymentRepo = paymentRepo;
        this.systemMessagesUtil = systemMessagesUtil;
    }

    @GetMapping("/admin_payments")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_payments")) {
            paymentList = paymentRepo.findAll();
            model.addAttribute("paymentList", paymentList);
            error = "";
            news = "";
            return "admin_payments";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/change-payment")
    public String changePayment(@ModelAttribute Payment payment, @RequestParam("id") Long idPayment) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_payments")) {
            Payment chosenPayment = paymentRepo.findByIdPayment(idPayment);
            session.setAttribute("chosenPayment", chosenPayment);
            session.setAttribute("chosenPaymentId", chosenPayment.getIdPayment());
            return "redirect:/admin_payments_edit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/change-realization-date")
    public String changeRealizeDate(@ModelAttribute Payment payment, @RequestParam("id") Long idPayment) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_payments")) {
            Payment chosenPayment = paymentRepo.findByIdPayment(idPayment);
            chosenPayment.setRealizationDate(new Date());
            paymentRepo.save(chosenPayment);
            systemMessagesUtil.sendMessageFromSystem(chosenPayment.getIdLearner(), "Your payment is now confirmed in system. Payment: " + chosenPayment.getReason() + " with price: " + chosenPayment.getPrice());
            return "redirect:/admin_payments";
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
