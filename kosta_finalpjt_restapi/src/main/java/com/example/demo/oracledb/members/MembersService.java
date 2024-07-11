package com.example.demo.oracledb.members;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.oracledb.depts.Depts;
import com.example.demo.oracledb.depts.DeptsDao;
import com.example.demo.oracledb.depts.Joblvs;
import com.example.demo.oracledb.depts.JoblvsDao;
import com.example.demo.oracledb.users.Users;
import com.example.demo.oracledb.users.UsersDao;

/*
 * ==================================================================
 * 추가 및 전체수정
 * id로 검색
 * 이름으로 검색
 * 전체 검색
 * 부서번호로 검색
 * 직급으로 검색
 * ==================================================================
*/

@Service
public class MembersService {
	@Autowired
	private MembersDao mdao;

	@Autowired
	private UsersDao udao;

	@Autowired
	private DeptsDao ddao;

	@Autowired
	private JoblvsDao jdao;

	public MembersDto save(MembersDto dto) {
		Members m = mdao.save(new Members(dto.getUserid(), dto.getMemberid(), dto.getBirthdt(), dto.getEmail(),
				dto.getCpnum(), dto.getAddress(), dto.getMemberimgnm(), dto.getHiredt(), dto.getLeavedt(),
				dto.getDeptid(), dto.getJoblvid(), dto.getMgrid(), null));
		return new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
				m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
				m.getMgrid(), null, null);
	}

	public MembersDto dummyMembersave(String dummyuserid) {
		//
		List<String> yearArr0 = Arrays.asList("19", "20");
		List<String> yearArr1 = Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
				"12", "13", "14", "15", "16", "17", "18");
		List<String> monthArr = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
		List<String> dayArr = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
				"30");

		Collections.shuffle(yearArr1);
		Collections.shuffle(monthArr);
		Collections.shuffle(dayArr);
		String text = yearArr0.get(0);
		System.out.println(text);
		if (text.equals("19")) {
			text += ((int) (Math.random() * 69) + 30) + "";
		} else if (text.equals("20")) {
			Collections.shuffle(yearArr0);
			text += yearArr0.get(0);
		}

		String birthdtText = text + "/" + monthArr.get(0) + "/" + dayArr.get(0);
		System.out.println(birthdtText);
		DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate ldate = LocalDate.parse(birthdtText, dtformatter);
		//
		Random random = new Random();
		String[] addressRndArr = { "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울상광역시", "세종특별자치시", "경기도",
				"강원특별자치도", "충청북도", "충청남도", "전북특별자치도", "전라남도", "경상북도", "경상남도", "제주특별자치도" };
		String addressRnd = addressRndArr[random.nextInt(addressRndArr.length)];
		//
		List<Depts> dlist = ddao.findAll();
		Collections.shuffle(dlist);
		//
		List<Joblvs> jlist = jdao.findAll();
		Collections.shuffle(jlist);
		Users u = udao.findById(dummyuserid).orElse(null);
		MembersDto mdto = new MembersDto(u, 0, ldate, (u.getId() + "@email.com"), "000-0000-0000", addressRnd, null,
				null, null, dlist.get(0), jlist.get(0), null, null, null);
		if (u != null && mdao.findByUserid(u) == null) {
			Members m = mdao.save(new Members(u, mdto.getMemberid(), mdto.getBirthdt(), mdto.getEmail(),
					mdto.getCpnum(), mdto.getAddress(), mdto.getMemberimgnm(), mdto.getHiredt(), mdto.getLeavedt(),
					mdto.getDeptid(), mdto.getJoblvid(), mdto.getMgrid(), null));
			mdto = new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
					m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
					m.getMgrid(), null, null);
		} else {
			mdto = null;
//			return null;
		}
//		Members m = mdao.save(new Members(dto.getUserid(), dto.getMemberid(), dto.getBirthdt(), dto.getEmail(),
//				dto.getCpnum(), dto.getAddress(), dto.getMemberimgnm(), dto.getHiredt(), dto.getLeavedt(),
//				dto.getDeptid(), dto.getJoblvid(), dto.getMgrid(), null));
//		return new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
//				m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
//				m.getMgrid(), null, null);
		return mdto;
	}

//	public MembersDto update(MembersDto dto) {
//		mdao.update(dto.getMemberid(), dto.getBirthdt(), dto.getEmail(), dto.getCpnum(), dto.getAddress(), dto.getMemberimgnm(), dto.getHiredt(), dto.getLeavedt(), dto.getDeptid(), dto.getJoblvid(), dto.getMgrid());
//		return getByMemberId(dto.getMemberid());
//	}

	public MembersDto getByMemberId(int memberid) {
		Members m = mdao.findById(memberid).orElse(null);
		if (m == null) {
			return null;
		}
		return new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
				m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
				m.getMgrid(), null, null);
	}

	public MembersDto getByuserId(String userid) {
		Members m = mdao.findByUserid(new Users(userid, "", "", "", 1, null));
		if (m == null) {
			return null;
		}
		return new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
				m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
				m.getMgrid(), null, null);
	}

	public MembersDto getByuserNm(Users userid) {
		Members m = mdao.findByUserid(userid);
		if (m == null) {
			return null;
		}
		return new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
				m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
				m.getMgrid(), null, null);
	}

	public ArrayList<MembersDto> getAll() {
		List<Members> l = mdao.findAll();
		ArrayList<MembersDto> list = new ArrayList<>();
		for (Members m : l) {
			list.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
					m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
					m.getMgrid(), null, null));
		}
		return list;
	}

//	public ArrayList<MembersDto> getByDeptNm(String deptnm) {
//		List<Members> l = mdao.findByDeptid(new Depts(0, deptnm, null));
//		ArrayList<MembersDto> list = new ArrayList<>();
//		for (Members m : l) {
//			list.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
//					m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
//					m.getMgrid(), null, null));
//		}
//		return list;
//	}

//	public ArrayList<MembersDto> getByJobLv(String joblvnm) {
//		List<Members> l = mdao.findByJoblvid(new Joblvs(0, 0, joblvnm));
//		ArrayList<MembersDto> list = new ArrayList<>();
//		for (Members m : l) {
//			list.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
//					m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
//					m.getMgrid(), null, null));
//		}
//		return list;
//	}

	public ArrayList<MembersDto> getByDeptNmLike(String deptnm) {
		ArrayList<Depts> dlist = ddao.findByDeptnmLike("%" + deptnm + "%");
		ArrayList<MembersDto> list = new ArrayList<>();
		for (Depts d : dlist) {
			List<Members> l = mdao.findByDeptid(d);
			for (Members m : l) {
				if (m.getUserid().getAprov() == 1) {
					list.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
							m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(),
							m.getJoblvid(), m.getMgrid(), null, null));
				}
			}

		}
		return list;
	}

	public ArrayList<MembersDto> getByUsersLike(String usernm) {
		ArrayList<Users> ulist = udao.findByUsernmLike("%" + usernm + "%");
		ArrayList<MembersDto> list = new ArrayList<>();
		for (Users u : ulist) {
			Members m = mdao.findByUserid(u);
			list.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
					m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
					m.getMgrid(), null, null));
		}
		return list;

	}

	public ArrayList<MembersDto> getByJobLvLike(String joblvnm) {
		ArrayList<Joblvs> jlist = jdao.findByJoblvnmLike("%" + joblvnm + "%");
		ArrayList<MembersDto> list = new ArrayList<>();
		for (Joblvs j : jlist) {
			List<Members> l = mdao.findByJoblvid(j);
			for (Members m : l) {
				if (m.getUserid().getAprov() == 1) {
					list.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
							m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(),
							m.getJoblvid(), m.getMgrid(), null, null));
				}
			}
		}
		return list;
	}

}
