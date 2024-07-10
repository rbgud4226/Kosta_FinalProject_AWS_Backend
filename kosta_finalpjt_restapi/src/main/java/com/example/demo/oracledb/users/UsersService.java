package com.example.demo.oracledb.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.demo.oracledb.depts.Depts;
import com.example.demo.oracledb.depts.Joblvs;
import com.example.demo.oracledb.members.MembersDto;

/*
 * ==================================================================
 * 추가 및 전체수정
 * id로 검색
 * aprov으로 검색
 * 전체 검색
 * id로 삭제
 * ==================================================================
*/

@Service
public class UsersService {
	@Autowired
	private UsersDao dao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UsersDto save(UsersDto dto) {
		Users u = dao.save(new Users(dto.getId(), dto.getUsernm(), passwordEncoder.encode(dto.getOldpwd()), dto.getType(),
				dto.getAprov(), dto.getRoomUsers()));
		return new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
				new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
				u.getRoomUsers());
	}

	// 비밀번호 이외 수정
	public void update(UsersDto dto) {
		dao.update(dto.getId(), dto.getUsernm(), dto.getType(), dto.getAprov());
	}

	// 비밀번호 수정
	public void updatePwd(String id, String newpwd) {
		dao.updatePwd(id, passwordEncoder.encode(newpwd));
	}

	// user 더미 데이터 생성 용도
	public UsersDto dummyUsersave() {
		// id
		String testId = "testid" + (int) (Math.random() * 100);
		// usernm
		List<String> lastnm = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권",
				"황", "안", "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽",
				"성", "차", "주", "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현",
				"함", "변", "염", "양", "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표",
				"명", "기", "반", "왕", "금", "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구",
				"용");
		List<String> firstnm = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노",
				"누", "다", "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미",
				"민", "바", "박", "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소",
				"솔", "수", "숙", "순", "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요",
				"용", "우", "원", "월", "위", "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전",
				"정", "제", "조", "종", "주", "준", "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택",
				"판", "하", "한", "해", "혁", "현", "형", "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부",
				"림", "봉", "혼", "황", "량", "린", "을", "비", "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱",
				"휴", "언", "령", "섬", "들", "견", "추", "걸", "삼", "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식",
				"란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠", "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개",
				"롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
		Collections.shuffle(lastnm);
		Collections.shuffle(firstnm);
		String testNm = lastnm.get(0) + firstnm.get(0) + firstnm.get(1);
		//
		String type = "emp";
		//
		System.out.println("testid:" + testId);
		System.out.println("testnm:" + testNm);
		int aprov = 0;
		if (dao.findById(testId) != null) {
			Users u = dao.save(new Users(testId, testNm, passwordEncoder.encode("111"), type, aprov, null));
			System.out.println("u:" + u);
			return new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
					new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
					u.getRoomUsers());
		}
		return null;
	}

	public UsersDto getById(String id) {
		Users u = dao.findById(id).orElse(null);
		if (u == null) {
			return null;
		}
		return new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
				new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
				u.getRoomUsers());
	}

	public UsersDto getByUsernm(String usernm) {
		Users u = dao.findByUsernm(usernm);
		if (u == null) {
			return null;
		}
		return new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
				new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
				u.getRoomUsers());
	}

	public ArrayList<UsersDto> getByAprov(int aprov) {
		List<Users> l = dao.findByAprov(aprov);
		ArrayList<UsersDto> list = new ArrayList<UsersDto>();
		for (Users u : l) {
			list.add(new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
					new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
					u.getRoomUsers()));
		}
		return list;
	}

	public ArrayList<UsersDto> getByIdLike(String id) {
		List<Users> l = dao.findByIdLike("%" + id + "%");
		ArrayList<UsersDto> list = new ArrayList<UsersDto>();
		for (Users u : l) {
			list.add(new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
					new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
					u.getRoomUsers()));
		}
		return list;
	}

	public ArrayList<UsersDto> getByUsernmLike(String usernm) {
		List<Users> l = dao.findByUsernmLike("%" + usernm + "%");
		ArrayList<UsersDto> list = new ArrayList<UsersDto>();
		for (Users u : l) {
			list.add(new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
					new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
					u.getRoomUsers()));
		}
		return list;
	}

	public ArrayList<UsersDto> getAll() {
		List<Users> l = dao.findAll();
		ArrayList<UsersDto> list = new ArrayList<UsersDto>();
		for (Users u : l) {
			list.add(new UsersDto(u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(),
					new MembersDto(u, 0, null, null, null, null, null, null, null, null, null, null, null, null),
					u.getRoomUsers()));
		}
		return list;
	}

	public ArrayList<UsersDto> getbyDepid(int deptid) {
		List<Users> l = dao.findAll();
		ArrayList<UsersDto> list = new ArrayList<UsersDto>();
		for (Users u : l) {
			list.add(new UsersDto(
					u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(), new MembersDto(u, 0, null,
							null, null, null, null, null, null, new Depts(deptid, null, null), null, null, null, null),
					u.getRoomUsers()));
		}
		return list;
	}

	public ArrayList<UsersDto> getbyJoblv(int joblv) {
		List<Users> l = dao.findAll();
		ArrayList<UsersDto> list = new ArrayList<UsersDto>();
		for (Users u : l) {
			list.add(new UsersDto(
					u.getId(), u.getUsernm(), u.getPwd(), null, null, u.getType(), u.getAprov(), new MembersDto(u, 0, null,
							null, null, null, null, null, null, null, new Joblvs(0, joblv, ""), null, null, null),
					u.getRoomUsers()));
		}
		return list;
	}

	public void delMember(String id) {
		dao.deleteById(id);
	}
	
	// 회원가입 유효성 검사
	@Transactional(readOnly = true)
	public Map<String, String> validateHandling(Errors errors) {
		Map<String, String> validatorResult = new HashMap<>();

		for (FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}

	// 채팅용 임시
	public Users getById2(String id) {
		Users u = dao.findById(id).orElse(null);
		return u;
	}

}
