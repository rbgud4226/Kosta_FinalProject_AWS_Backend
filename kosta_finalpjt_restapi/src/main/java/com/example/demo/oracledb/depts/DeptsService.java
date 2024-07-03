package com.example.demo.oracledb.depts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.oracledb.members.Members;

@Service
public class DeptsService {
	@Autowired
	private DeptsDao dao;

	public DeptsDto save(DeptsDto dto) {
		Depts d = dao.save(new Depts(dto.getDeptid(), dto.getDeptnm(), dto.getMgrid()));
		return new DeptsDto(d.getDeptid(), d.getDeptnm(), d.getMgrid());
	}

	// dept 더미 데이터 생성 용도
	public DeptsDto dummyDeptsave() {
		List<Depts> dlist = dao.findAll();
		int deptidNum = dlist.get(dlist.size() - 1).getDeptid() + 10;
		String testDept = "dept_test" + deptidNum;
		if (dao.findByDeptnmLike(testDept) != null) {
			DeptsDto ddto = new DeptsDto(deptidNum, testDept, null);
			Depts d = dao.save(new Depts(ddto.getDeptid(), ddto.getDeptnm(), ddto.getMgrid()));
			return new DeptsDto(d.getDeptid(), d.getDeptnm(), d.getMgrid());
		}
		return null;
	}

	public DeptsDto getByDeptId(int deptid) {
		Depts d = dao.findById(deptid).orElse(null);
		if (d == null) {
			return null;
		}
		return new DeptsDto(d.getDeptid(), d.getDeptnm(), d.getMgrid());
	}

	public ArrayList<DeptsDto> getByDeptNm(String deptnm) {
		List<Depts> l = dao.findByDeptnmLike("%" + deptnm + "%");
		ArrayList<DeptsDto> list = new ArrayList<DeptsDto>();
		for (Depts d : l) {
			list.add(new DeptsDto(d.getDeptid(), d.getDeptnm(), d.getMgrid()));
		}
		return list;
	}

	public ArrayList<DeptsDto> getByMgrId(int mgrid) {
		List<Depts> l = dao.findByMgrid(
				new Members(null, mgrid, null, null, null, null, null, null, null, null, null, null, null));
		ArrayList<DeptsDto> list = new ArrayList<DeptsDto>();
		for (Depts d : l) {
			list.add(new DeptsDto(d.getDeptid(), d.getDeptnm(), d.getMgrid()));
		}
		return list;
	}

	public ArrayList<DeptsDto> getAll() {
		List<Depts> l = dao.findAll();
		ArrayList<DeptsDto> list = new ArrayList<DeptsDto>();
		for (Depts d : l) {
			list.add(new DeptsDto(d.getDeptid(), d.getDeptnm(), d.getMgrid()));
		}
		return list;
	}

	public void delDepts(int deptid) {
		dao.deleteById(deptid);
	}
}
