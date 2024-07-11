package com.example.demo.oracledb.notice;

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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
	@Id
	@SequenceGenerator(name="seq_notice", sequenceName="seq_noti", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_noti")
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Users writer;
	private String startdt;
	private String enddt;
	private String title;
	private String content;
	private String formtype;
	private String writername;

	@PrePersist
	public void setDate() {
		Date now = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd aHH시"); 
		String strNowDate = simpleDateFormat.format(now); 
		startdt = strNowDate;
	}
}
