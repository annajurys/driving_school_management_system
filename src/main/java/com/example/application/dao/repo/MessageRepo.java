package com.example.application.dao.repo;

import com.example.application.dao.Message;
import com.example.application.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findAllByIdTo(long id);

    List<Message> findAllByIdToAndIdFrom(long idTo, long idFrom);

    List<Message> findAllByIdToOrIdToAndIdFromOrIdFrom(long idTo, long idTo2, long idFrom, long idFrom2);
}
