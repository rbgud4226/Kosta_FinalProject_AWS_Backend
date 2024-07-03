package com.example.demo.oracledb.members;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EduWorkExperienceInfoService {
	@Autowired
	private EduWorkExperienceInfoDao edao;

	@Autowired
	private MembersDao mdao;

	public EduWorkExperienceInfoDto save(EduWorkExperienceInfoDto dto) {
		EduWorkExperienceInfo ewe = edao.save(new EduWorkExperienceInfo(dto.getEweid(), dto.getMemberid(),
				dto.getStartdt(), dto.getEnddt(), dto.getEwenm1(), dto.getEwenm2(), dto.getType(), dto.getState()));
		return new EduWorkExperienceInfoDto(ewe.getEweid(), ewe.getMemberid(), ewe.getStartdt(), ewe.getEnddt(),
				ewe.getEwenm1(), ewe.getEwenm2(), ewe.getType(), ewe.getState());
	}

	public ArrayList<EduWorkExperienceInfoDto> getByMembers(int memberid) {
		List<EduWorkExperienceInfo> l = edao.findByMemberid(new Members(null, memberid, null, null, null, null, null, null, null, null, null, null, null));
		ArrayList<EduWorkExperienceInfoDto> list = new ArrayList<EduWorkExperienceInfoDto>();
		for (EduWorkExperienceInfo ewe : l) {
			list.add(new EduWorkExperienceInfoDto(ewe.getEweid(), ewe.getMemberid(), ewe.getStartdt(), ewe.getEnddt(),
					ewe.getEwenm1(), ewe.getEwenm2(), ewe.getType(), ewe.getState()));
		}
		return list;
	}

	public ArrayList<EduWorkExperienceInfoDto> getAll() {
		List<EduWorkExperienceInfo> l = edao.findAll();
		ArrayList<EduWorkExperienceInfoDto> list = new ArrayList<>();
		for (EduWorkExperienceInfo ewe : l) {
			list.add(new EduWorkExperienceInfoDto(ewe.getEweid(), ewe.getMemberid(), ewe.getStartdt(), ewe.getEnddt(),
					ewe.getEwenm1(), ewe.getEwenm2(), ewe.getType(), ewe.getState()));
		}
		return list;
	}

	public void delEduWorkExperienceInfo(int eweid ) {
		edao.deleteById(eweid);
	}
}
