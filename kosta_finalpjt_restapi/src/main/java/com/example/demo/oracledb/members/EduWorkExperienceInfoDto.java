package com.example.demo.oracledb.members;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class EduWorkExperienceInfoDto {
	private int eweid;
	private Members memberid;
	private LocalDate startdt;
	private LocalDate enddt;
	private String ewenm1;
	private String ewenm2;
	private int state;
	private int type;
}
