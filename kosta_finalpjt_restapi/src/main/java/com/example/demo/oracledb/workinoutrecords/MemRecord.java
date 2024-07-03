package com.example.demo.oracledb.workinoutrecords;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

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

//직원 월별 기록 조회용
public class MemRecord {
	private String dayOfWeek;
	private LocalDate day;
	private String workHours;
	private String state;
}
