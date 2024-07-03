package com.example.demo.oracledb.members;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * ==================================================================
 * 
 * 추가 여부 확인 필요 멤버변수
	//	// 자동부여, 참조키
	//	private Date wupdateddt;
	//	// 자동부여, 참조키
	//	private Date aprovupdateddt;
 * ==================================================================
*/

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class EduWorkExperienceInfo {
	@Id
	@SequenceGenerator(name = "seq_gen", sequenceName = "seq_eweid", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eweid")
	private int eweid;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Members memberid;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startdt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate enddt;
	private String ewenm1;
	private String ewenm2;
	private int state;
	private int type;

}
