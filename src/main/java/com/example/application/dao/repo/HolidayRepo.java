package com.example.application.dao.repo;

import com.example.application.dao.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HolidayRepo extends JpaRepository<Holiday, Long> {
    List<Holiday> findByIdUser(Long id);

    List<Holiday> findByIdUserAndDateToAfter(Long id, Date date);

    Holiday findByIdHoliday(Long id);
}
