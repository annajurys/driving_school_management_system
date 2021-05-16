package com.example.application.utils;

import com.example.application.dao.Payment;
import com.example.application.dao.repo.MessageRepo;
import com.example.application.dao.repo.PaymentRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class SystemPaymentUtil {

    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private PaymentRepo paymentRepo;
    private MessageRepo messageRepo;

    @Autowired
    public SystemPaymentUtil(UserRepo userRepo, UserDetailRepo userDetailRepo, PaymentRepo paymentRepo, MessageRepo messageRepo) {
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.paymentRepo = paymentRepo;
        this.messageRepo = messageRepo;
    }

    public void addPaymentFromSystem(Long id, int price, String reason) {
        Payment payment = new Payment();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 30);
        Date date = c.getTime();
        payment.setEndDate(date);
        payment.setIdLearner(id);
        payment.setPrice(price);
        payment.setRealizationDate(null);
        payment.setReason(reason);
        payment.setUserDetail(userDetailRepo.findByIdUserDetail(id));
        payment.setUser(userRepo.findByIdUser(id));
        paymentRepo.save(payment);
    }
}
