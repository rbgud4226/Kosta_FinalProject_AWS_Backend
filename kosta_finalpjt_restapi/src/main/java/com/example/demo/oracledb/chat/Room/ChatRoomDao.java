package com.example.demo.oracledb.chat.Room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.oracledb.chat.RoomUser.RoomUser;

import jakarta.transaction.Transactional;


@Repository
public interface ChatRoomDao extends JpaRepository<ChatRoom, String> {
	ChatRoom findByName(String name);
	ChatRoom findByChatroomid(String chatroomid);
	List<ChatRoom> findByRoomUsers(List<RoomUser> roomUsers);
	List<ChatRoom> findByParticipants(String participants);
	List<ChatRoom> findByStatus(boolean status);
	@Transactional
	void deleteByStatus(boolean status);
}
