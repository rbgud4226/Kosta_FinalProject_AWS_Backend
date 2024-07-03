package com.example.demo.oracledb.workinoutrecords;

import java.time.LocalDate;

import com.example.demo.oracledb.members.Members;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkInOutRecordDto {
	private int daynum;
	private Members user;
	private String dayOfWeek;
	private LocalDate day;
	private String workinTime;
	private String workOutTime;
	private String workHours;
	private String state;
}
