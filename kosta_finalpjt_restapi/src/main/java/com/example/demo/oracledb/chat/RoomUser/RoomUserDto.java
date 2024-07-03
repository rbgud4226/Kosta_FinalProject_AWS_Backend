package com.example.demo.oracledb.chat.RoomUser;

import com.example.demo.oracledb.chat.Room.ChatRoom;
import com.example.demo.oracledb.users.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomUserDto {
	private Long id;
	private ChatRoom room;
	private Users roomuser;
}

