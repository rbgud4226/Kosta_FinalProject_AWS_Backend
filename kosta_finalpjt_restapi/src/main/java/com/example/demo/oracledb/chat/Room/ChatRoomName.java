package com.example.demo.oracledb.chat.Room;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomName {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomName_seq")
	@SequenceGenerator(name = "roomName_seq", sequenceName = "roomName_sequence", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	@JsonBackReference
	private ChatRoom room;
	
	private String host;
	private String roomName;
	private String editableName;

}
