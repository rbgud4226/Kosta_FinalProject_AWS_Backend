package com.example.demo.oracledb.charts;

import com.example.demo.oracledb.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartsDao extends JpaRepository<Charts, Integer > {
  List<Charts> findByUsersOrderByTaskidDesc(Users id);
}
