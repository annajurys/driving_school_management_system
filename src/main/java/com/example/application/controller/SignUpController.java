package com.example.application.controller;

import com.example.application.dao.Payment;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.PaymentRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.PasswordSHA1;
import com.example.application.dao.User;
import com.example.application.utils.SystemMessagesUtil;
import com.example.application.utils.SystemPaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class SignUpController {

    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private PasswordSHA1 passwordSHA1;
    private String error;
    private String news;
    private SystemMessagesUtil systemMessagesUtil;
    private SystemPaymentUtil systemPaymentUtil;

    @Autowired
    public SignUpController(UserRepo userRepo, UserDetailRepo userDetailRepo, PasswordSHA1 passwordSHA1, SystemMessagesUtil systemMessagesUtil, SystemPaymentUtil systemPaymentUtil) {
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.passwordSHA1 = passwordSHA1;
        this.systemMessagesUtil = systemMessagesUtil;
        this.systemPaymentUtil = systemPaymentUtil;
    }

    @RequestMapping("/sign-up")
    public String get(Model model, HttpSession session) {
        if (null == session.getAttribute("role")) {
            model.addAttribute("newUser", new User());
            model.addAttribute("newUserDetail", new UserDetail());
            error = "";
            news = "";
            return "sign_up";
        } else if (session.getAttribute("role").equals("learner")) {
            return "redirect:/learner_home";
        } else if (session.getAttribute("role").equals("instructor")) {
            return "redirect:/driving_instructor_home";
        } else {
            return "redirect:/admin_home";
        }
    }

    @PostMapping("/save-sign-up")
    public String addUser(@ModelAttribute User user, @ModelAttribute UserDetail userDetail, @RequestParam("roles") String role, @RequestParam("name") String name, @RequestParam("surname") String surname, @RequestParam("address") String address, @RequestParam("telephone") String telephone) {
        if (userRepo.existsUserByEmail(user.getEmail())) {
            error = "This email already exists";
            return "redirect:/sign-up";
        }
        if (!telephone.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            error = "Please enter correct telephone number";
            return "redirect:/sign-up";
        }
        user.setRole(role);
        user.setPassword(passwordSHA1.encryptPassword(user.getPassword()));
        userRepo.save(user);


        userDetail.setName(name);
        userDetail.setSurname(surname);
        userDetail.setAddress(address);
        userDetail.setTelephone(telephone);
        userDetail.setUser(userRepo.findByEmail(user.getEmail()));
        userDetail.setAvatar("https://eu.ui-avatars.com/api/?rounded=true&bold=true&name=" + name + "+" + surname);
        userDetailRepo.save(userDetail);

        systemMessagesUtil.sendMessageFromSystem(userRepo.findByEmail(user.getEmail()).getIdUser(), "Thank you for joining our community!");
        if (role.contains("learner")) {
            systemPaymentUtil.addPaymentFromSystem(userRepo.findByEmail(user.getEmail()).getIdUser(), 200, "Medical examination");
            systemPaymentUtil.addPaymentFromSystem(userRepo.findByEmail(user.getEmail()).getIdUser(), 2000, "Course");
        }

        if (role.equals("learner")) {
            return "redirect:/learner_home";
        } else if (role.equals("instructor")) {
            return "redirect:/driving_instructor_home";
        }
        return "redirect:/sign-up";
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
