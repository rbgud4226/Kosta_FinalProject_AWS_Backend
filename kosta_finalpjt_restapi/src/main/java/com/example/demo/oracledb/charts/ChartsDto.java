package com.example.demo.oracledb.charts;

import com.example.demo.oracledb.users.Users;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChartsDto {
  private Users users;
  private int taskid;
  private String chartResource;
  private String title;
  private String st;
  private String ed;
  private int percent;
  private String dependencies;
  private String chartStatus;
}
