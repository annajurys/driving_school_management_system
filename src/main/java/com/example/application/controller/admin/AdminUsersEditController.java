package com.example.application.controller.admin;

import com.example.application.dao.Lesson;
import com.example.application.dao.Message;
import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
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
public class AdminUsersEditController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private String error;
    private String news;

    @Autowired
    public AdminUsersEditController(HttpSession session, SessionUtil sessionUtil, UserDetailRepo userDetailRepo, UserRepo userRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
    }

    @GetMapping("/admin_users_edit")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_users_edit")) {
            User chosenUser = (User) session.getAttribute("chosenUser");
            model.addAttribute("editUser", chosenUser);
            error = "";
            news = "";
            return "admin_users_edit";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/save-admin-users-edit")
    public String saveAdminUsersEdit(@ModelAttribute UserDetail userDetail, @RequestParam("name") String name, @RequestParam("surname") String surname, @RequestParam("address") String address, @RequestParam("telephone") String telephone) {
        UserDetail userDetail1 = userDetailRepo.findByIdUserDetail((Long) session.getAttribute("chosenUserId"));
        userDetail1.setName(name);
        userDetail1.setSurname(surname);
        userDetail1.setAddress(address);
        userDetail1.setAvatar("https://eu.ui-avatars.com/api/?rounded=true&bold=true&name=" + name + "+" + surname);
        if (!telephone.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$") && telephone != "null") {
            error = "Please enter correct telephone number";
            return "redirect:/admin_users_edit";
        } else {
            userDetail1.setTelephone(telephone);
        }
        userDetailRepo.save(userDetail1);
        session.removeAttribute("chosenUser");
        session.removeAttribute("chosenUserId");
        return "redirect:/admin_users";
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
