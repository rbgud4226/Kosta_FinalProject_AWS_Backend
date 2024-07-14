package com.example.demo.oracledb.chat.Message;



import java.sql.Timestamp;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.oracledb.chat.Room.ChatRoom;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
	@SequenceGenerator(name = "chat_seq", sequenceName = "chat_sequence", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonBackReference
	private ChatRoom room;

	@Column(length = 2000)
	private String content;
	private Timestamp sendDate;
	private String sender;
	private String type;
	private String fileName;
	private String fileId;
	private String fileRoot;
	private String partid;

	public Message(Long id, ChatRoom room, Timestamp sendDate, String sender, String type, String partid) {
		super();
		this.id = id;
		this.room = room;
		this.sendDate = sendDate;
		this.sender = sender;
		this.type = type;
		this.partid = partid;
	}
}
