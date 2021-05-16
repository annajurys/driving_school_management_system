package com.example.application.controller.admin;

import com.example.application.dao.Message;
import com.example.application.dao.repo.MessageRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminHomeController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private MessageRepo messageRepo;
    private List<Message> messageList;
    private List<Message> messageUnreadList;

    @Autowired
    public AdminHomeController(HttpSession session, SessionUtil sessionUtil, MessageRepo messageRepo) {
        this.session = session;
        this.sessionUtil = sessionUtil;
        this.messageRepo = messageRepo;
    }

    @GetMapping("/admin_home")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/admin_home")) {
            error = "";
            news = "";

            messageList = new ArrayList<>();
            messageList = messageRepo.findAllByIdTo((Long) session.getAttribute("id"));
            messageUnreadList = new ArrayList<>();
            for (Message message : messageList) {
                if (!message.getIsRead()) {
                    messageUnreadList.add(message);
                }
            }

            model.addAttribute("messageUnreadList", messageUnreadList);
            return "admin_home";
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
