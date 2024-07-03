package com.example.demo.oracledb.chat.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomNameDto {
	private Long id;
	private ChatRoom room;
	private String host;
	private String roomName;
	private String editableName;
}
