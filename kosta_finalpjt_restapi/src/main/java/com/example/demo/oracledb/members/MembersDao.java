package com.example.demo.oracledb.members;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.oracledb.depts.Depts;
import com.example.demo.oracledb.depts.Joblvs;
import com.example.demo.oracledb.users.Users;

import jakarta.transaction.Transactional;


@Repository
public interface MembersDao extends JpaRepository<Members, Integer> {
	
	Members findByUserid(Users userid);
	
	ArrayList<Members> findByDeptid(Depts deptid);

	ArrayList<Members> findByJoblvid(Joblvs joblvid);

	@Transactional
	@Modifying
	@Query(value = "update members set "
			+ "birthdt=:birthdt, "
			+ "email=:email, "
			+ "cpnum=:cpnum, "
			+ "address=:address, "
			+ "memberimgnm=:memberimgnm, "
			+ "hiredt=:hiredt, "
			+ "leavedt=:leavedt, "
			+ "depts_deptid=:deptid, "
			+ "joblvs_joblvid=:joblvid, "
			+ "mgrid=:mgrid, "
			+ "where memberid=:memberid", nativeQuery = true)
	void update(@Param("memberid") int memberid
			, @Param("birthdt") LocalDate birthdt
			, @Param("email") String email
			, @Param("cpnum") String cpnum
			, @Param("address") String address
			, @Param("memberimgnm") String memberimgnm
			, @Param("hiredt") LocalDate hiredt
			, @Param("leavedt") LocalDate leavedt
			, @Param("deptid") Depts deptid
			, @Param("joblvid") Joblvs joblvid
			, @Param("mgrid") Members mgrid
			
			
			);
}
