package com.example.application.dao.repo;

import com.example.application.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByRole(String role);

    User findByEmail(String email);

    boolean existsUserByEmail(String email);

    User findByIdUser(long id);
}
