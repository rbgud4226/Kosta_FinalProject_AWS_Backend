package com.example.demo.oracledb.depts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoblvsService {

	@Autowired
	private JoblvsDao dao;

	public JoblvsDto save(JoblvsDto dto) {
		Joblvs j = dao.save(new Joblvs(dto.getJoblvidx(), dto.getJoblvid(), dto.getJoblvnm()));
		if (j == null) {
			return null;
		}
		return new JoblvsDto(j.getJoblvidx(), j.getJoblvid(), j.getJoblvnm());
	}

	// joblv 더미 데이터 생성 용도
	public JoblvsDto dummyJoblvsave() {
		List<Joblvs> jlist = dao.findAll();
		int joblvidNum = jlist.get(jlist.size() - 1).getJoblvid() + 100;
		String testjoblv = "joblv_test" + joblvidNum;
		if (dao.findByJoblvnmLike(testjoblv) != null) {
			JoblvsDto jdto = new JoblvsDto(0, joblvidNum, testjoblv);
			Joblvs j = dao.save(new Joblvs(jdto.getJoblvidx(), jdto.getJoblvid(), jdto.getJoblvnm()));
			return new JoblvsDto(j.getJoblvidx(), j.getJoblvid(), j.getJoblvnm());
		}
		return null;
	}

	public JoblvsDto getByJoblvIdx(int joblvidx) {
		Joblvs j = dao.findById(joblvidx).orElse(null);
		if (j == null) {
			return null;
		}
		return new JoblvsDto(j.getJoblvidx(), j.getJoblvid(), j.getJoblvnm());
	}

	public ArrayList<JoblvsDto> getByJoblvId(int joblvid) {
		List<Joblvs> l = dao.findByJoblvid(joblvid);
		ArrayList<JoblvsDto> list = new ArrayList<JoblvsDto>();
		for (Joblvs j : l) {
			list.add(new JoblvsDto(j.getJoblvidx(), j.getJoblvid(), j.getJoblvnm()));
		}
		return list;
	}

	public ArrayList<JoblvsDto> getByjoblvnmLike(String joblvnm) {
		List<Joblvs> l = dao.findByJoblvnmLike("%" + joblvnm + "%");
		ArrayList<JoblvsDto> list = new ArrayList<JoblvsDto>();
		for (Joblvs j : l) {
			list.add(new JoblvsDto(j.getJoblvidx(), j.getJoblvid(), j.getJoblvnm()));
		}
		return list;
	}

	public ArrayList<JoblvsDto> getAll() {
		List<Joblvs> l = dao.findAll();
		ArrayList<JoblvsDto> list = new ArrayList<JoblvsDto>();
		for (Joblvs j : l) {
			list.add(new JoblvsDto(j.getJoblvidx(), j.getJoblvid(), j.getJoblvnm()));
		}
		return list;
	}

	public void delJoblvs(int joblvidx) {
		dao.deleteById(joblvidx);
	}
}
