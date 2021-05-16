package com.example.application.dao.repo;

import com.example.application.dao.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    List<Payment> findByIdLearner(Long id);

    Payment findByIdPayment(Long id);
}
