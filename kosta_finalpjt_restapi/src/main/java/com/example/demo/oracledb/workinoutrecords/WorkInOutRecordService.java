package com.example.demo.oracledb.workinoutrecords;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkInOutRecordService {
	@Autowired
	private WorkInOutRecordDao dao;
		
	//당일 출퇴근 기록 여부 조회
	public ArrayList<WorkInOutRecordDto> selectByDate(int user) {
		ArrayList<WorkInOutRecord> wo = dao.selectDay(user);
		ArrayList<WorkInOutRecordDto> list = new ArrayList<>();
		for(WorkInOutRecord w : wo) {
			list.add(new WorkInOutRecordDto(w.getDaynum(),w.getUser(),w.getDayOfWeek(),w.getDay(),w.getWorkinTime(),w.getWorkOutTime(),w.getWorkHours(),w.getState()));
		}
		return list;
	}

	//출근,퇴근(save로 처음 등록, 퇴근 등록시 save로 수정)
	public WorkInOutRecordDto save(WorkInOutRecordDto dto) {
		WorkInOutRecord w = dao.save(new WorkInOutRecord(dto.getDaynum(),dto.getUser(),dto.getDayOfWeek(),dto.getDay(),dto.getWorkinTime(),dto.getWorkOutTime(),dto.getWorkHours(),dto.getState()));
		return new WorkInOutRecordDto(w.getDaynum(),w.getUser(),w.getDayOfWeek(),w.getDay(),w.getWorkinTime(),w.getWorkOutTime(),w.getWorkHours(),w.getState());
	}
	
	//한개 기록 조회용(퇴근용)
	public WorkInOutRecordDto select(int memberid) {
		WorkInOutRecord w = dao.findById(memberid).orElse(null);
		if(w == null) {
			return null;
		}
		return new WorkInOutRecordDto(w.getDaynum(),w.getUser(),w.getDayOfWeek(),w.getDay(),w.getWorkinTime(),w.getWorkOutTime(),w.getWorkHours(),w.getState());
	}

	//직원 월별 기록 조회
	public ArrayList<MemRecord> selectUser(int month,int year,int user){
		ArrayList<WorkInOutRecord> wo = dao.selectMonthByUser(month, year, user);
		ArrayList<MemRecord> list = new ArrayList<>();
		for(WorkInOutRecord w : wo) {
			list.add(new MemRecord(w.getDayOfWeek(),w.getDay(),w.getWorkHours(),w.getState()));
		}
		return list;
	}
		
	//부서 직원 통계기록
	public ArrayList<ChartDeptMember> chartMonthandDept(int month,int year,int dept){
		List<Object[]> deptlist = dao.chartDept(month, year, dept);
		ArrayList<ChartDeptMember> list = new ArrayList<ChartDeptMember>();
		for(Object[] a: deptlist) {
			ChartDeptMember c = new ChartDeptMember();
			c.setId(((Number) a[0]).intValue());
			c.setName((String) a[1]);
			c.setDeptNum((String) a[2]); 
			c.setJoblv((String) a[3]);
			c.setTotalRecords(((Number) a[4]).intValue());
			c.setLateCount(((Number) a[5]).intValue());
			c.setWorkTime((String) a[6]);
			c.setOverWork(((String) a[7]));
			list.add(c);
		}
		return list;
	}
	
	//관리자 부서 월별 근무시간 조회
	public ArrayList<DeptsYearWorkData> deptYearData (int year,int dept) {
		List<Object[]> deptlist = dao.deptMonthWork(year,dept);
		ArrayList<DeptsYearWorkData> list = new ArrayList<DeptsYearWorkData>();
		
		for(Object[] a: deptlist) {
			DeptsYearWorkData d = new DeptsYearWorkData();
		    d.setDeptnum(dept);
		    d.setMonth((String) a[0]);
		    d.setWorkhours(((Number) a[1]).intValue());
		    list.add(d);
		}
		return list;
	}
	
	//전체 직원 추가 근무 기록 조회(월별)
	public ArrayList<OverWorkData> overStatistics (int year,int month) {
		List<Object[]> overWorkData = dao.overWorkData(year,month);
		ArrayList<OverWorkData> list = new ArrayList<OverWorkData>();
		
		for(Object[] a: overWorkData) {
			OverWorkData o = new OverWorkData();
			o.setLess30(((Number) a[0]).intValue());
			o.setLess1hour(((Number) a[1]).intValue());
			o.setLess2hours(((Number) a[2]).intValue());
			o.setOver2hours(((Number) a[3]).intValue());
		    list.add(o);
		}
		return list;
	}
	
	
}
