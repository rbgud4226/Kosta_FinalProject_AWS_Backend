package com.example.demo.oracledb.docx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface DocxDao extends JpaRepository<Docx, Integer> {
	
	 // 작성자로 문서를 검색하고 중복을 제거하여 페이징 처리된 결과를 반환하는 메서드
    @Query(value = "SELECT * FROM (SELECT d.*, ROWNUM rnum FROM (SELECT * FROM Docx WHERE writer_id = :writerId AND docxorder = 0 ORDER BY formnum DESC) d WHERE ROWNUM <= :endRow) WHERE rnum >= :startRow",
           nativeQuery = true)
    List<Docx> findDistinctByWriter(@Param("writerId") String writerId, @Param("startRow") int startRow, @Param("endRow") int endRow);

    // 제목에 해당하는 문서를 Like 검색하여 페이징 처리된 결과를 반환하는 메서드
    @Query(value = "SELECT * FROM (SELECT d.*, ROWNUM rnum FROM (SELECT * FROM Docx WHERE title LIKE :title AND docxorder = 0 ORDER BY formnum DESC) d WHERE ROWNUM <= :endRow) WHERE rnum >= :startRow",
           nativeQuery = true)
    List<Docx> findByTitleLike(@Param("title") String title, @Param("startRow") int startRow, @Param("endRow") int endRow);

	// formtype으로 검색 num 값으로 정렬후 출력
	List<Docx> findByFormtypeOrderByFormnumDesc(String formtype);

	// docxkey로 검색해서 그 문서안에 전체 시니어 검색하는 메서드
	List<Docx> findByDocxkeyAndFormtype(int docxkey, String formtype);

	// docxkey값을 기준으로 중복되는 문서 정리해주는 메서드
	List<Docx> findDistinctByDocxkey(int docxkey);

	// 중복제거 해서 전체 리스트 가져오기
	List<Docx> findByDocxorder(int docxorder);

//	// 승인된 문서 목록 페이징 조회
//    @Query(value = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM DOCX WHERE STATUS = 1 ORDER BY FORMNUM DESC) A WHERE ROWNUM <= ?2) WHERE RNUM >= ?1", nativeQuery = true)
//    List<Docx> findApprovedDocxWithPagination(int startRow, int endRow);
//
//    // 미승인된 문서 목록 페이징 조회
//    @Query(value = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM DOCX WHERE STATUS = 0 ORDER BY FORMNUM DESC) A WHERE ROWNUM <= ?2) WHERE RNUM >= ?1", nativeQuery = true)
//    List<Docx> findUnapprovedDocxWithPagination(int startRow, int endRow);

	@Query(value = "SELECT * FROM (SELECT a.*, ROWNUM rnum FROM (SELECT * FROM docx where docxorder = 0 ORDER BY formnum DESC) a where ROWNUM <= :endRow) WHERE rnum >= :startRow", nativeQuery = true)
	List<Docx> findAllByDocxorderWithPagination(@Param("startRow") int startRow, @Param("endRow") int endRow);

	// 전체 리스트의 수를 반환하는 쿼리
	@Query("SELECT COUNT(d) FROM Docx d WHERE d.docxorder = 0")
	int countByDocxorder(int docxorder);

	@Query(value = "SELECT * FROM (" + "    SELECT d.*, ROWNUM rnum FROM (" + "        SELECT * FROM docx"
			+ "        WHERE writer_id = :writerId AND docxorder = 0" + "        ORDER BY formnum DESC" + "    ) d"
			+ "    WHERE ROWNUM <= :endRow" + ")" + "WHERE rnum >= :startRow", nativeQuery = true)
	List<Docx> findUserDocxWithPaginationAndDocxOrder(@Param("writerId") String writer, @Param("startRow") int startRow,
			@Param("endRow") int endRow);

	@Query("SELECT COUNT(DISTINCT d) FROM Docx d WHERE d.writer.id = :writerId")
	int countUserDocx(@Param("writerId") String writerId);
	
//	// 승인된 문서 총 개수
//    @Query("SELECT COUNT(d) FROM Docx d WHERE d.status = 1")
//    int countApprovedDocx();
//
//    // 미승인된 문서 총 개수
//    @Query("SELECT COUNT(d) FROM Docx d WHERE d.status = 0")
//    int countUnapprovedDocx();

	// Senior이름으로 검색
	List<Docx> findBySenior(String senior);
	
	// docxkey 값이 같은 문서를 전부 삭제하는 메서드
	@Modifying
	@Transactional
	@Query("DELETE FROM Docx d WHERE d.docxkey = :docxkey")
	void deleteByDocxkey(int docxkey);

}
