package com.example.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessageId(Integer id);
    Optional<Integer> deleteByMessageId(Integer id);
    Optional<ArrayList<Message>> findAllByPostedBy(int accountId);
}
