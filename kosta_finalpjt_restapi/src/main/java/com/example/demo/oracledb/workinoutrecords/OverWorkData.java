package com.example.demo.oracledb.workinoutrecords;

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
//추가 근무 조회용
public class OverWorkData {
	private int less30;
	private int less1hour;
	private int less2hours;
	private int over2hours;
}
