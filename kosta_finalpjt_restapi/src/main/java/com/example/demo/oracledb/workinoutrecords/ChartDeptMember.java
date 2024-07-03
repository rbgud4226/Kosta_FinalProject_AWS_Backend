package com.example.demo.oracledb.workinoutrecords;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

//부서장용 월별 직원 근무 기록
public class ChartDeptMember {
	//사원번호 이름 부서번호 직급레벨 총_출근횟수 지각횟수 총_근무시간 추가근무 시간
	private int id;
	private String name;
	private String deptNum;
	private String joblv;
	private int totalRecords;
	private int lateCount;
	private String workTime;
	private String overWork;
}
