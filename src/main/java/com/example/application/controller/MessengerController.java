package com.example.application.controller;

import com.example.application.dao.Message;
import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.MessageRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;

@Controller
public class MessengerController {

    private UserRepo userRepo;
    private HttpSession session;
    private MessageRepo messageRepo;
    private SessionUtil sessionUtil;
    private String error;
    private String news;
    private List<Message> messageList;
    private List<Message> messageGoodList;
    private UserDetailRepo userDetailRepo;
    private List<User> userList;

    @Autowired
    public MessengerController(UserRepo userRepo, UserDetailRepo userDetailRepo, HttpSession session, MessageRepo messageRepo, SessionUtil sessionUtil) {
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.session = session;
        this.messageRepo = messageRepo;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping("/messenger")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/messenger")) {
            Long idFrom = 1L;
            userList = userRepo.findAll();
            messageList = new ArrayList<>();
            messageGoodList = new ArrayList<>();
            if (null != session.getAttribute("idFrom")) {
                idFrom = (Long) session.getAttribute("idFrom");
                messageList = messageRepo.findAll();
                for (Message message : messageList) {
                    if ((message.getIdFrom() == idFrom && message.getIdTo() == (Long) session.getAttribute("id")) || (message.getIdFrom() == (Long) session.getAttribute("id") && message.getIdTo() == idFrom)) {
                        messageGoodList.add(message);
                        if (message.getIdFrom() != (Long) session.getAttribute("id")) {
                            message.setIsRead(true);
                            messageRepo.save(message);
                        }
                    }
                }
                model.addAttribute("sele", idFrom);
            } else {
                session.setAttribute("idFrom", 1L);
                idFrom = 1L;
                messageList = messageRepo.findAll();
                for (Message message : messageList) {
                    if ((message.getIdFrom() == idFrom && message.getIdTo() == (Long) session.getAttribute("id")) || (message.getIdFrom() == (Long) session.getAttribute("id") && message.getIdTo() == idFrom)) {
                        messageGoodList.add(message);
                        if (message.getIdFrom() != (Long) session.getAttribute("id")) {
                            message.setIsRead(true);
                            messageRepo.save(message);
                        }
                    }
                }
                model.addAttribute("sele", idFrom);
            }


            model.addAttribute("sele", idFrom);
            model.addAttribute("userList", userList);
            model.addAttribute("newMessage", new Message());
            model.addAttribute("checkUser", new User());
            model.addAttribute("messageList", messageList);
            model.addAttribute("messageGoodList", messageGoodList);
            error = "";
            news = "";
            return "messenger";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/save-messenger")
    public String addMesssenger(@ModelAttribute User user, @ModelAttribute Message message, @RequestParam("messageContent") String messageContent, HttpSession session) {
        if (userRepo.existsById((Long) session.getAttribute("idFrom"))) {
            message.setIdFrom((Long) session.getAttribute("id"));
            message.setIdTo((Long) session.getAttribute("idFrom"));
            message.setDate(new Date());
            if (messageContent != "") {
                message.setMessageContent(messageContent);
            } else {
                error = "You can not send empty message";
                return "redirect:/messenger";
            }
            message.setIsRead(false);
            message.setUserDetailFrom(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")));
            message.setUserDetailTo(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("idFrom")));
            messageRepo.save(message);
        } else {
            error = "User does not exist";
        }
        return "redirect:/messenger";
    }

    @RequestMapping("/sum")
    public String sum(@RequestParam("user") long user) {
        session.setAttribute("idFrom", user);
        return "redirect:/messenger";
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
