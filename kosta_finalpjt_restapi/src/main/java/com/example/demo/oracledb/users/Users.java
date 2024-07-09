package com.example.demo.oracledb.users;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.example.demo.oracledb.chat.RoomUser.RoomUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@DynamicInsert

public class Users {
	@Id
	private String id;
	private String usernm;
	private String pwd;
	@ColumnDefault("emp")
	private String type;
	@ColumnDefault("0")
	private int aprov;

	@OneToMany(mappedBy = "roomuser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonIgnore
	@ToString.Exclude
	private List<RoomUser> roomUsers = new ArrayList<RoomUser>();
}
