package com.example.demo.oracledb.chat.RoomUser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.oracledb.chat.Room.ChatRoom;
import com.example.demo.oracledb.users.Users;

@Service
public class RoomUserService {
	@Autowired
	private RoomUserDao roomuserdao;

	public void save(Users roomuser, ChatRoom room) {
		if (!roomuserdao.findByRoomAndRoomuser(room, roomuser).isPresent()) {
			roomuserdao.save(new RoomUser(null, room, roomuser));
		}
	}

	public ArrayList<RoomUserDto> findRoom(String chatroomid) {
		List<RoomUser> l = roomuserdao.findByRoom_Chatroomid(chatroomid);
		ArrayList<RoomUserDto> list = new ArrayList<>();
		for (RoomUser ru : l) {
			list.add(new RoomUserDto(ru.getId(), ru.getRoom(), ru.getRoomuser()));
		}
		return list;
	}
	
	public void deleteRoomUsersByChatroomid(String chatroomid) {
		roomuserdao.deleteByRoom_chatroomid(chatroomid);
	}

}
