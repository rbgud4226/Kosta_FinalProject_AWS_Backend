package com.example.demo.oracledb.depts;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.oracledb.members.Members;

@Repository
public interface DeptsDao extends JpaRepository<Depts, Integer> {
	ArrayList<Depts> findByDeptnmLike(String deptnm);
	ArrayList<Depts> findByMgrid(Members mgrid);
}
