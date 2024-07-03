package com.example.demo.oracledb.members;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduWorkExperienceInfoDao extends JpaRepository<EduWorkExperienceInfo, Integer> {
	ArrayList<EduWorkExperienceInfo> findByMemberid(Members memberid);
}
