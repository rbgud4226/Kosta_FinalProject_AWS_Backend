package com.example.demo.oracledb.chat.Message;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends JpaRepository<Message, Long> {
	List<Message> findByRoom_ChatroomidOrderByIdAsc(String chatroomid);
	Page<Message> findByRoom_ChatroomidOrderBySendDateDesc(String roomId, Pageable pageable);
}
