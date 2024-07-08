package com.example.demo.oracledb.chat.Room;

import java.util.List;

import com.example.demo.oracledb.chat.Message.Message;
import com.example.demo.oracledb.chat.RoomUser.RoomUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
	private String chatroomid;
	private String name;
	@JsonManagedReference
	private List<ChatRoomName> chatRoomNames;
	private String roomType;
	@JsonManagedReference
	private List<Message> chats;
	@JsonIgnore
	private List<RoomUser> roomUsers;
	private boolean status;
	private String recentMsg;
	private String participants;
	private String img;
	private List<String> memberNames;
}
