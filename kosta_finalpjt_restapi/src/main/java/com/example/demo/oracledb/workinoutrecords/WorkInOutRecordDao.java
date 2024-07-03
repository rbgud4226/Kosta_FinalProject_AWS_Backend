package com.example.demo.oracledb.workinoutrecords;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkInOutRecordDao extends JpaRepository<WorkInOutRecord, Integer> {
	//오늘 날짜 등록 여부 확인
	@Query(value="SELECT * FROM workinoutrecord WHERE user_id =:user_id and day >= TRUNC(SYSDATE) AND day < TRUNC(SYSDATE) + 1",nativeQuery = true)
	ArrayList<WorkInOutRecord> selectDay(@Param("user_id")int user);
	
	//연/월별 (부서)전체 직원 조회
	@Query(value="SELECT usernm, day, dayofweek, workinTime, workOutTime, workhours, state " +
			"from workinoutrecord W join members M on W.user_id = M.memberid " + 
			"join users u on u.id = m.userid_id " + 
			"WHERE EXTRACT(MONTH FROM W.day) = :month " + 
			"AND EXTRACT(YEAR FROM W.day) = :year " +
			"AND DEPTIDS_DEPTID = :dept " + 
			"ORDER by day", nativeQuery = true)
	List<Object[]> selectMonth(@Param("month")int month,@Param("year")int year,@Param("dept")int dept);

	//연/월별 (부서)직원 통계
	//사원번호(user_id) 이름 부서번호 직급레벨 총_출근횟수 지각횟수 총_근무시간
	@Query(value = "SELECT \r\n"
			+ "    M.memberid, u.USERNM, d.deptnm, j.joblvnm, \r\n"
			+ "    COUNT(*) AS total_records,\r\n"
			+ "    SUM(CASE WHEN W.state = '지각' THEN 1 ELSE 0 END) AS total_late_records,\r\n"
			+ "    LPAD(FLOOR(SUM(TO_NUMBER(SUBSTR(workhours, 1, 2)) * 60 + TO_NUMBER(SUBSTR(workhours, 4, 2))) / 60), 3, '0')\r\n"
			+ "    || ':' || \r\n"
			+ "    LPAD(MOD(SUM(TO_NUMBER(SUBSTR(workhours, 1, 2)) * 60 + TO_NUMBER(SUBSTR(workhours, 4, 2))), 60), 2, '0') AS total_time,\r\n"
			+ "    LPAD(FLOOR(SUM(CASE WHEN TO_NUMBER(SUBSTR(workhours, 1, 2)) >= 18 THEN TO_NUMBER(SUBSTR(workhours, 1, 2)) - 18 ELSE 0 END * 60\r\n"
			+ "                    + CASE WHEN TO_NUMBER(SUBSTR(workouttime, 1, 2)) >= 18 THEN TO_NUMBER(SUBSTR(workouttime, 4, 2)) ELSE 0 END) / 60), 2, '0')\r\n"
			+ "    || ':' || \r\n"
			+ "    LPAD(MOD(SUM(CASE WHEN TO_NUMBER(SUBSTR(workouttime, 1, 2)) >= 18 THEN TO_NUMBER(SUBSTR(workouttime, 1, 2)) - 18 ELSE 0 END * 60\r\n"
			+ "                 + CASE WHEN TO_NUMBER(SUBSTR(workouttime, 1, 2)) >= 18 THEN TO_NUMBER(SUBSTR(workouttime, 4, 2)) ELSE 0 END), 60), 2, '0') AS over_work_time\r\n"
			+ "FROM workinoutrecord W\r\n"
			+ "JOIN members M ON W.user_id = M.memberid\r\n"
			+ "JOIN users u ON u.id = M.userid_id\r\n"
			+ "JOIN joblvs j ON j.joblvidx = M.joblvs_joblvid\r\n"
			+ "JOIN depts d ON d.deptid = M.depts_deptid\r\n"
			+ "WHERE EXTRACT(MONTH FROM W.day) = :month\r\n"
			+ "AND EXTRACT(YEAR FROM W.day) = :year\r\n"
			+ "AND M.depts_deptid = :dept\r\n"
			+ "GROUP BY M.memberid, u.USERNM, d.deptnm, j.joblvnm", nativeQuery = true)
    List<Object[]> chartDept(@Param("month") int month, @Param("year") int year, @Param("dept") int dept);
	
	//개인의 월(연) 근태기록 조회
	@Query(value="SELECT *	FROM workinoutrecord WHERE EXTRACT(MONTH FROM day) = :month AND EXTRACT(YEAR FROM day) = :year and user_id =:user_id ORDER by day",nativeQuery = true)
	ArrayList<WorkInOutRecord> selectMonthByUser(@Param("month")int month,@Param("year")int year,@Param("user_id")int user);

	//관리자용
	//월별 부서간 근무시간 통계
	@Query(value = "WITH 근무시간계산 AS (\r\n"
			+ "    SELECT\r\n"
			+ "        TO_CHAR(TO_DATE(SUBSTR(day, 1, 8), 'YY/MM/DD'), 'YYYY') AS 연도,\r\n"
			+ "        TO_CHAR(TO_DATE(SUBSTR(day, 1, 8), 'YY/MM/DD'), 'MM') AS 월,\r\n"
			+ "        SUM(TO_NUMBER(SUBSTR(workhours, 1, 2)) * 60 + TO_NUMBER(SUBSTR(workhours, 4, 2))) AS 총근무분,\r\n"
			+ "        COUNT(DISTINCT w.user_id) AS 근무자수\r\n"
			+ "    FROM workinoutrecord w\r\n"
			+ "    JOIN members m ON w.user_id = m.memberid\r\n"
			+ "    WHERE TO_CHAR(TO_DATE(SUBSTR(day, 1, 8), 'YY/MM/DD'), 'YYYY') = :year "
			+ "    and m.depts_deptid = :dept \r\n"
			+ "    GROUP BY m.depts_deptid, TO_CHAR(TO_DATE(SUBSTR(day, 1, 8), 'YY/MM/DD'), 'YYYY'), TO_CHAR(TO_DATE(SUBSTR(day, 1, 8), 'YY/MM/DD'), 'MM')\r\n"
			+ ")\r\n"
			+ "SELECT "
			+ "    월,"
			+ "     NVL(TRUNC(총근무분 / 60/근무자수), 0) AS 평균근무시간_시간 "
			+ "FROM 근무시간계산 "
			+ "ORDER BY 연도, 월", nativeQuery = true)
    List<Object[]> deptMonthWork(@Param("year") int year,@Param("dept")int dept);
    
    
	//전체 직원 추가 근무 시간 통계용
	@Query(value = "SELECT"
			+ "    COUNT(CASE WHEN WORKOUTTIME > '18:10' AND WORKOUTTIME <= '18:30' THEN 1 END) ,\n"
			+ "    COUNT(CASE WHEN WORKOUTTIME > '18:30' AND WORKOUTTIME <= '19:00' THEN 1 END) ,\n"
			+ "    COUNT(CASE WHEN WORKOUTTIME > '19:00' AND WORKOUTTIME <= '21:00' THEN 1 END) ,\n"
			+ "    COUNT(CASE WHEN WORKOUTTIME > '21:00' THEN 1 END) \n"
			+ "FROM\n"
			+ "    workinoutrecord W\n"
			+ "WHERE EXTRACT(year FROM W.day) = :year \n"
			+ "AND EXTRACT(MONTH FROM W.day) = :month \n"
			+ "GROUP BY\n"
			+ "    EXTRACT(MONTH FROM day)", nativeQuery = true)
    List<Object[]> overWorkData(@Param("year") int year,@Param("month")int month);

}
