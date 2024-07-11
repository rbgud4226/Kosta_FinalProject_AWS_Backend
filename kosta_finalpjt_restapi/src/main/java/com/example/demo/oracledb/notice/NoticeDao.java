package com.example.demo.oracledb.notice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;





@Repository
public interface NoticeDao extends JpaRepository<Notice, Long> {
	Optional<Notice> findById(Long id);
	List<Notice> findByFormtype(String formtype);
	Page<Notice> findAll(Pageable pageable);
	Page<Notice> findByFormtype(String formtype, Pageable pageable);
	Page<Notice> findByTitleContaining(String title,  Pageable pageable);
	Page<Notice> findByWriter_UsernmContaining(String writer, Pageable pageable);

}
