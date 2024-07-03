package com.example.demo.oracledb.depts;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoblvsDao extends JpaRepository<Joblvs, Integer> {
	ArrayList<Joblvs> findByJoblvid(int joblvid);
	ArrayList<Joblvs> findByJoblvnmLike(String joblvnm);
}
