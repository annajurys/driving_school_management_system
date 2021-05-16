package com.example.application.dao.repo;

import com.example.application.dao.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByIdLearner(Long id);

    List<Comment> findByIdDrivingInstructor(Long id);
}
