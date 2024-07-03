package com.example.demo.oracledb.docx;


import com.example.demo.oracledb.users.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocxDto {
	private int formnum;
	private Users writer;
	private String senior;
	private String startdt;
	private String enddt;
	private String title;
	private String content;
	private String note; // 비고 작성
	private int taskclasf; //업무 구분
	private String taskplan; // 업무 계획내용
	private String taskprocs; // 업무 진행과정
	private String taskprocsres; // 업무 진행 결과
	private String deptandmeetloc; //회의 진행 장소
	private String	dayoffclasf; //휴가 구분
	private String participant; //회의 참석자
	private String formtype; //문서 타입 구분
	private int aprovdoc; // 결제서류 승인 여부 : 거절 보류 승인
	private int docxorder; //결재 순서
	private int status; // 결재 현황
	private int docxkey;
	private int orderloc; // 현재 결재위치
	
}
