package com.example.application.utils;

import com.example.application.dao.Message;
import com.example.application.dao.User;
import com.example.application.dao.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Enumeration;

@Service
public class SystemMessagesUtil {

    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private PaymentRepo paymentRepo;
    private MessageRepo messageRepo;

    @Autowired
    public SystemMessagesUtil(UserRepo userRepo, UserDetailRepo userDetailRepo, PaymentRepo paymentRepo, MessageRepo messageRepo) {
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.paymentRepo = paymentRepo;
        this.messageRepo = messageRepo;
    }

    public void sendMessageFromSystem(Long id, String messageContent) {
        Message message = new Message();
        message.setIdFrom(1);
        message.setIdTo(id);
        message.setIsRead(false);
        message.setDate(new Date());
        message.setMessageContent(messageContent);
        message.setUserDetailFrom(userDetailRepo.findByIdUserDetail(1L));
        message.setUserDetailTo(userDetailRepo.findByIdUserDetail(id));
        messageRepo.save(message);
    }
}
