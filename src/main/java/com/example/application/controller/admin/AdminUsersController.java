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
public class AdminUsersController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private List<User> userList;
    private List<UserDetail> userDetailList;
    private String error;
    private String news;

    @Autowired
    public AdminUsersController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, UserDetailRepo userDetailRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
    }

    @GetMapping("/admin_users")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_users")) {
            userList = userRepo.findAll();
            userDetailList = userDetailRepo.findAll();
            model.addAttribute("userList", userList);
            model.addAttribute("userDetailList", userDetailList);
            error = "";
            news = "";
            return "admin_users";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/change-user")
    public String changeUser(@ModelAttribute User user, @RequestParam("id") Long idUser) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_users")) {
            User chosenUser = userRepo.findByIdUser(idUser);
            session.setAttribute("chosenUser", chosenUser);
            session.setAttribute("chosenUserId", chosenUser.getIdUser());
            return "redirect:/admin_users_edit";
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
