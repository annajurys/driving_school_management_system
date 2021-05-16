package com.example.application.dao.repo;

import com.example.application.dao.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Long> {
    List<Lesson> findByIdLearnerOrderByDateAsc(Long id);

    List<Lesson> findByIdDrivingInstructorOrderByDateAsc(Long id);

    List<Lesson> findByDateAfterAndIdDrivingInstructorOrderByDateAsc(Date date, Long id);

    Lesson findByDate(Date date);

    Lesson findByIdLesson(Long id);
}
