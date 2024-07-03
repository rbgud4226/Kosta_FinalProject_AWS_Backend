package com.example.demo.oracledb.users;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.oracledb.chat.RoomUser.RoomUser;

import jakarta.transaction.Transactional;

/*
 *
 *
 *
*/

@Repository
public interface UsersDao extends JpaRepository<Users, String> {
	Users findByUsernm(String usernm);

	ArrayList<Users> findByAprov(int aprov);

	ArrayList<Users> findByIdLike(String id);

	ArrayList<Users> findByUsernmLike(String usernm);

	ArrayList<Users> findAllByRoomUsers(RoomUser roomuser);
	
	@Transactional
	@Modifying
	@Query(value = "update users set id=:id, usernm=:usernm, type=:type, aprov=:aprov where id=:id", nativeQuery = true)
	void update(@Param("id") String id, @Param("usernm") String usernm, @Param("type") String type,
			@Param("aprov") int aprov);

	@Transactional
	@Modifying
	@Query(value = "update users set pwd=:pwd where id=:id", nativeQuery = true)
	void updatePwd(@Param("id") String id, @Param("pwd") String pwd);
}
