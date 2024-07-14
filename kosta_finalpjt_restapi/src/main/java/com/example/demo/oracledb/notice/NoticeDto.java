package com.example.demo.oracledb.notice;

import com.example.demo.oracledb.users.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
	private Long id;
	private Users writer;
	private String startdt;
	private String enddt;
	private String title;
	private String content;
	private String formtype;
	private String writername;
	
}
