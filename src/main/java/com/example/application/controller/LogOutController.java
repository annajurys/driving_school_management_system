package com.example.application.controller;

import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@Controller
public class LogOutController {

    private SessionUtil sessionUtil;
    private HttpSession session;

    @Autowired
    public LogOutController(SessionUtil sessionUtil, HttpSession session) {
        this.sessionUtil = sessionUtil;
        this.session = session;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        sessionUtil.endSession(session);
        return "redirect:/";
    }
}
