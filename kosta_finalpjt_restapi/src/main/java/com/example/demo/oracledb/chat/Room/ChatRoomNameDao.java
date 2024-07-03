package com.example.demo.oracledb.chat.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ChatRoomNameDao extends JpaRepository<ChatRoomName, Long> {
	List<ChatRoomName> findByRoom_chatroomid(String chatroomid);
}
