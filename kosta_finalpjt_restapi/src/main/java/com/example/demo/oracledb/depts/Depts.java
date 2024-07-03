package com.example.demo.oracledb.depts;

import com.example.demo.oracledb.members.Members;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Depts {
	@Id
	@SequenceGenerator(name = "seq_gen", sequenceName = "seq_deptid", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_deptid")
	private int deptid;
	private String deptnm;

	@OneToOne
	@JoinColumn(name = "mgrid")
	private Members mgrid;
}
