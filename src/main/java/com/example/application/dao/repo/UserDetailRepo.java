package com.example.application.dao.repo;

import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailRepo extends JpaRepository<UserDetail, Long> {
    UserDetail findByUser(User user);

    UserDetail findByIdUserDetail(Long id);
}
