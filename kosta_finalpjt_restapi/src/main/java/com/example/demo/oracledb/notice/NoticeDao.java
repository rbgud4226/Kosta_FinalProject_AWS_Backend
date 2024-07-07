package com.example.demo.oracledb.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface NoticeDao extends JpaRepository<Notice, Long> {
	List<Notice> findByTitle(String title);
}
