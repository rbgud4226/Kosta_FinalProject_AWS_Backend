package com.example.demo.oracledb.docx;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.oracledb.users.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
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
@Entity
public class Docx {
	@Id
	@SequenceGenerator(name="seq_gen", sequenceName="seq_docx", allocationSize=1)//시퀀스 생성
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_docx")//num에 시퀀스로 값 자동할당
	private int formnum;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Users writer;
	private String senior;
	private String startdt;
	private String enddt;
	private String title;
	private String content;
	private String note; // 비고 작성
	private int taskclasf; //업무 구분 
	private String taskplan; // 업무 계획내용 , 휴가구분
	private String taskprocs; // 업무 진행과정 , 회의일시
	private String taskprocsres; // 업무 진행 결과, 회의장소
	private String deptandmeetloc; //회의 진행 장소
	private String	dayoffclasf; //휴가 구분
	private String participant; //참석자
	private String formtype; //문서 타입 구분
	private int aprovdoc; // 결재서류 승인 여부 : 거절 보류 승인
	private int docxorder; //결재 순서
	private int status; // 결재 현황
	private int docxkey; //유니크 값
	private int orderloc; // 현재 결재위치
	
	
	
	
	@PrePersist
	public void setDate() {
		Date now = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy년MM월dd일 a HH시"); 
		String strNowDate = simpleDateFormat.format(now); 
		startdt = strNowDate;
	}

}
