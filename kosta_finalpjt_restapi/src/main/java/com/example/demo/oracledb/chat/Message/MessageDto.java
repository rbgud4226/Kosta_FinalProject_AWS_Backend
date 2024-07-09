package com.example.demo.oracledb.chat.Message;

import java.sql.Timestamp;

import com.example.demo.oracledb.chat.Room.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
	private Long id;
	private ChatRoom room;
	private String content;
	private Timestamp sendDate;
	private String sender;
	private String type;
	private String fileName;
	private String fileId;
	private String fileRoot;
	private String partid;
	private String username;

	public MessageDto(Long id, ChatRoom room, Timestamp sendDate, String sender, String type, String partid) {
		super();
		this.id = id;
		this.room = room;
		this.sendDate = sendDate;
		this.sender = sender;
		this.type = type;
		this.partid = partid;
	}
}
