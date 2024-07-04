//package com.example.demo.mariadb.users;
//
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface virtual_users_dao extends JpaRepository<virtual_users, Integer> {
//
//  @Query(value = "INSERT INTO virtual_users (domain_id, password, email, box) " +
//      "VALUES (:domainId, ENCRYPT(:password, CONCAT('$6$', SUBSTRING(SHA(RAND()), -16))), :email, :box)",
//      nativeQuery = true)
//  @Modifying
//  @Transactional
//  void insertVirtualUser(@Param("domainId") int domainId, @Param("password") String password,
//                         @Param("email") String email, @Param("box") String box);
//
//  virtual_users findByBox (String box);
//}
