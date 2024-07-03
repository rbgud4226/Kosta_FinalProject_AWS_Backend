package com.example.demo.oracledb.depts;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

public class Joblvs {
	@Id
	@SequenceGenerator(name = "seq_gen", sequenceName = "seq_joblvidx", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_joblvidx")
	private int joblvidx;
	private int joblvid;
	private String joblvnm;
}
