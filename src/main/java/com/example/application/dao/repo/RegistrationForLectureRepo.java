package com.example.application.dao.repo;

import com.example.application.dao.RegistrationForLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationForLectureRepo extends JpaRepository<RegistrationForLecture, Long> {
    boolean existsByIdLearner(Long id);

    RegistrationForLecture findByIdLearner(Long id);

    List<RegistrationForLecture> findByIdLecture(Long id);
}
