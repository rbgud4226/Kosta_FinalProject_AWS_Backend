package com.example.demo.oracledb.members;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.oracledb.depts.Depts;
import com.example.demo.oracledb.depts.Joblvs;
import com.example.demo.oracledb.users.Users;

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

public class MembersDto {
	private Users userid;
	private int memberid;
	private LocalDate birthdt;
	private String email;
	private String cpnum;
	private String address;
	private String memberimgnm;
	private LocalDate hiredt;
	private LocalDate leavedt;
	private Depts deptid;
	private Joblvs joblvid;
	private Members mgrid;
	private ArrayList<EduWorkExperienceInfo> eweinfo;
	private MultipartFile memberimgf;

}
