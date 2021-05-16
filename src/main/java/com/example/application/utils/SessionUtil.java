package com.example.application.utils;

import com.example.application.dao.User;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Service
public class SessionUtil {

    @Autowired
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;

    public void startSession(HttpSession session, User user, String role) {
        session.setAttribute("user", user);
        session.setAttribute("id", userRepo.findByEmail(user.getEmail()).getIdUser());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("password", user.getPassword());
        session.setAttribute("role", role);
    }

    public String endSession(HttpSession session) {
        session.setAttribute("user", null);
        session.setAttribute("id", null);
        session.setAttribute("email", null);
        session.setAttribute("password", null);
        session.setAttribute("role", null);
        return "redirect:/";
    }

    public void giveMeSessionStatus(HttpSession session) {
        Enumeration e = (Enumeration) (session.getAttributeNames());
        System.out.println("---------------- SESSION ATTRIBUTES: -----------");
        while (e.hasMoreElements()) {
            Object tring;
            if ((tring = e.nextElement()) != null) {
                System.out.println(tring + ":  " + session.getAttribute((String) tring));
                System.out.println("-----------");
            }
        }
    }

    public boolean checkIfThisRoleCanEnterHere(HttpSession session, String mapping) {
        if (null == session.getAttribute("role")) {
            if (mapping == "/")
                return true;
        }
        if (null != session.getAttribute("role") && mapping.contains("/messages"))
            return true;
        if (null != session.getAttribute("role") && mapping.contains("/messenger"))
            return true;
        if (null != session.getAttribute("role")) {
            if (session.getAttribute("role").equals("learner")) {
                if (mapping.contains("learner"))
                    return true;
            }
            if (session.getAttribute("role").equals("instructor")) {
                if (mapping.contains("instructor"))
                    return true;
            }
            if (session.getAttribute("role").equals("admin")) {
                if (mapping.contains("admin"))
                    return true;
            }
        }
        return false;
    }
}
