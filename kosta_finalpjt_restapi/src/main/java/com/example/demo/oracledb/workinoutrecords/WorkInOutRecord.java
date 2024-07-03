package com.example.demo.oracledb.workinoutrecords;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.oracledb.members.Members;

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
import lombok.ToString;

/*
 * ==================================================================
 * 
 * 추가 여부 확인 필요 멤버변수
	//	private Members memberid;
	//	private Date workdt;
	//	private Date workintime;
	//	private Date workouttime;
	//	private String state;
	//	private String workinstate;
	//	private String workoutstate;
 * ==================================================================
*/

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkInOutRecord {
	@Id
	@SequenceGenerator(name = "seq_gen", sequenceName = "seq_time", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_time")
	private int daynum;
	
	@ManyToOne
	@JoinColumn(name = "User_id")
	private Members user;
	//요일
	private String dayOfWeek;
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private LocalDate day;
	private String workinTime;
	private String workOutTime;
	private String workHours;
	private String state;
//	출근
//	정상근무
//	지각
//	야근
//	휴무
	@PrePersist
	public void setDate() {
		if (day != null && dayOfWeek != null) {
	        return;
	    }
		LocalTime currentTime = LocalTime.now();
		day = LocalDate.now();
        dayOfWeek = day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        workinTime = String.format("%02d:%02d",currentTime.getHour(), currentTime.getMinute());
	}
}