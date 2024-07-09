package com.example.demo.oracledb.chat.Message;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends JpaRepository<Message, Long> {
	List<Message> findByRoom_ChatroomidOrderByIdAsc(String chatroomid);

	@Query("SELECT m FROM Message m WHERE m.room.chatroomid = :roomId AND m.sendDate BETWEEN :fromTime AND :toTime ORDER BY m.sendDate ASC")
	Page<Message> findMessagesByRoom_ChatroomidAndSendDateBetween(@Param("roomId") String roomId,
			@Param("fromTime") LocalDateTime fromTime, @Param("toTime") LocalDateTime toTime, Pageable pageable);
}
