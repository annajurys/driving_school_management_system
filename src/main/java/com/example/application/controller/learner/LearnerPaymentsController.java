package com.example.application.controller.learner;

import com.example.application.dao.Payment;
import com.example.application.dao.repo.PaymentRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LearnerPaymentsController {

    private UserRepo userRepo;
    private HttpSession session;
    private SessionUtil sessionUtil;
    private PaymentRepo paymentRepo;
    private List<Payment> paymentList;
    private String error;
    private String news;

    @Autowired
    public LearnerPaymentsController(UserRepo userRepo, HttpSession session, SessionUtil sessionUtil, PaymentRepo paymentRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("/learner_payments")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/learner_payments")) {
            paymentList = paymentRepo.findByIdLearner((Long) session.getAttribute("id"));
            model.addAttribute("paymentList", paymentList);
            error = "";
            news = "";
            return "learner_payments";
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
