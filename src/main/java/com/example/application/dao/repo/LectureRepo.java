package com.example.application.dao.repo;

import com.example.application.dao.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LectureRepo extends JpaRepository<Lecture, Long> {
    Lecture findByIdLecture(Long id);

    List<Lecture> findAllByDate1AfterAndAvailableSeatsGreaterThan(Date date, int seats);
}
