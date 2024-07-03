package com.example.demo.oracledb.workinoutrecords;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

//관리자 페이지 선형 통계용
public class DeptsYearWorkData {
  private int deptnum;
  private String month;
  private int workhours;
}
