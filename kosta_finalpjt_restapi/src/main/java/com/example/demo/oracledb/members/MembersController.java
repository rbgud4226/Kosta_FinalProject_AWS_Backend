package com.example.demo.oracledb.members;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.oracledb.depts.DeptsDto;
import com.example.demo.oracledb.depts.DeptsService;
import com.example.demo.oracledb.depts.JoblvsDto;
import com.example.demo.oracledb.depts.JoblvsService;
import com.example.demo.oracledb.users.Users;
import com.example.demo.oracledb.users.UsersDto;
import com.example.demo.oracledb.users.UsersService;

@Controller
public class MembersController {

	@Autowired
	private MembersService mservice;

	@Autowired
	private UsersService uservice;

	@Autowired
	private DeptsService dservice;

	@Autowired
	private JoblvsService jservice;

	@Autowired
	private EduWorkExperienceInfoService eservice;

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${spring.servlet.multipart.location}")
	private String path;

	private String dirName = "/src/main/resources/static/img/member/";

	@GetMapping("/member/memberlist")
	public Map memberlist() {
		boolean flag = true;
		ArrayList<MembersDto> mlist = new ArrayList<MembersDto>();
		try {
			mlist = mservice.getAll();
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("mlist", mlist);
		return map;
//		return "member/memberlist";
	}

	@GetMapping("/member/test")
	public void membertest(@RequestParam(name = "userid", required = false) List<String> userids) {
		System.out.println(userids);
		for (String userid : userids) {
			System.out.println(userid);
		}

	}

	@ResponseBody
	@GetMapping("/member/getdeptby")
	public Map getmemberby(String val, int type) {
		boolean flag = true;
		ArrayList<MembersDto> mlist = new ArrayList<MembersDto>();
		try {
			if (!val.equals("")) {
				if (type == 1) {
					if (val.equals("0")) {
						mlist = null;
					} else {
						mlist = mservice.getByDeptNmLike(val);
					}
				} else if (type == 2) {
					ArrayList<UsersDto> ulist = uservice.getByUsernmLike(val);
					for (UsersDto udto : ulist) {
						if (udto != null
								&& mservice.getByuserNm(new Users(udto.getId(), "", "", "", 0, null)) != null) {
							mlist.add(mservice.getByuserNm(new Users(udto.getId(), "", "", "", 0, null)));
						}
					}
				} else if (type == 3) {
					mlist = mservice.getByJobLvLike(val);
				}
			} else {
				mlist = null;
			}
		} catch (Exception e) {
			flag = false;
		}
		System.out.println(mlist);
//		ModelAndView mav = new ModelAndView("member/memberlist");
//		mav.addObject("type", type);
//		mav.addObject("val", val);
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("mlist", mlist);
		return map;
	}

	@GetMapping("/member/memberinfo")
	public Map memberinfo(String id) {
		boolean flag = true;
		UsersDto udto = new UsersDto();
		MembersDto mdto = new MembersDto();

		ArrayList<EduWorkExperienceInfoDto> elist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<EduWorkExperienceInfoDto> edulist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<EduWorkExperienceInfoDto> expwoklist = new ArrayList<EduWorkExperienceInfoDto>();
		try {
			udto = uservice.getById(id);
			mdto = mservice.getByuserId(id);
			if (mservice.getByuserId(id) != null) {
				elist = eservice.getByMembers(mservice.getByuserId(id).getMemberid());
			}
			for (EduWorkExperienceInfoDto edto : elist) {
				if (edto.getType() == 0) {
					edulist.add(edto);
				} else {
					expwoklist.add(edto);
				}
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("user", udto);
		map.put("mdto", mdto);
		map.put("edulist", edulist);
		map.put("expwoklist", expwoklist);
		return map;
	}

	@GetMapping("/member/memberchatinfo")
	@ResponseBody
	public Map<String, Object> memberchatinfo(@RequestParam("userId") String userId) {
		MembersDto mdto = mservice.getByuserId(userId);
		DeptsDto deptN = dservice.getByDeptId(mdto.getDeptid().getDeptid());
		JoblvsDto jobL = jservice.getByJoblvIdx(mdto.getJoblvid().getJoblvidx());
		Map<String, Object> memchatinfo = new HashMap<>();
		memchatinfo.put("deptN", deptN);
		memchatinfo.put("jobL", jobL);
		memchatinfo.put("member", mdto);
		return memchatinfo;
	}

	@GetMapping("/member/memberimg")
	public ResponseEntity<byte[]> read_img(String memberimgnm) {
		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		if (memberimgnm != "") {
			File f = new File(path + dirName + memberimgnm);
			try {
				header.add("Content-Type", Files.probeContentType(f.toPath()));
				result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				InputStream istream = resourceLoader.getResource("classpath:/static/img/common/human.png")
						.getInputStream();
				header.setContentType(MediaType.IMAGE_PNG);
				result = new ResponseEntity<byte[]>(istream.readAllBytes(), header, HttpStatus.OK);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;
	}

	@GetMapping("/member/memberedit")
	public Map membereditform(String id) {
		boolean flag = true;
		MembersDto mdto = new MembersDto();
		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
		try {
			mdto = mservice.getByuserId(id);
			ArrayList<DeptsDto> dlistAll = dservice.getAll();
			for (DeptsDto ddto : dlistAll) {
				if (ddto.getMgrid() != null && ddto.getMgrid().getMemberid() == mservice
						.getByMemberId(ddto.getMgrid().getMemberid()).getMemberid()) {
					mdto = mservice.getByMemberId(ddto.getMgrid().getMemberid());
					ddto.setMgrid(new Members(mdto.getUserid(), mdto.getMemberid(), mdto.getBirthdt(), mdto.getEmail(),
							mdto.getCpnum(), mdto.getAddress(), mdto.getMemberimgnm(), mdto.getHiredt(),
							mdto.getLeavedt(), mdto.getDeptid(), mdto.getJoblvid(), mdto.getMgrid(), null));
					dlist.add(ddto);
				}
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("member", mdto);
		map.put("userid", id);
		map.put("dlist", dlist);
		map.put("jlist", jservice.getAll());
		return map;
	}

	@PostMapping("/member/memberadd")
	public Map memberadd(MembersDto dto, EduWorkExperienceInfoDto edto) {
		boolean flag = true;
		MembersDto mdto = new MembersDto();
		try {
			mdto = mservice.save(dto);
			if (!dto.getMemberimgf().isEmpty()) {
				String oname = dto.getMemberimgf().getOriginalFilename();
				String f1 = oname.substring(oname.lastIndexOf("."));
				String f2 = oname.substring(oname.lastIndexOf(".") + 1, oname.length());
				String f3 = oname.substring(0, oname.lastIndexOf("."));
				String fname = f3 + " (" + mdto.getUserid().getUsernm() + ")." + f2;
				File newFile = new File(path + dirName + fname);
				try {
					dto.getMemberimgf().transferTo(newFile);
					mdto.setMemberimgnm(newFile.getName());
//				System.out.println(mdto.getMemberimgnm());
					mservice.save(mdto);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("mdto.getMemberid():" + mdto.getMemberid());
				EduWorkExperienceInfoDto eweidto = eservice.save(edto);
				eweidto.setMemberid(new Members(null, mdto.getMemberid(), null, null, null, null, null, null, null,
						null, null, null, null));
				eservice.save(eweidto);
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("id", dto.getUserid().getId());
		return map;
	}

//	@PostMapping("/member/eweiadd")
//	public String eweiadd(EduWorkExperienceInfoDto edto) {
//		System.out.println("eweidto:" + edto);
//		EduWorkExperienceInfoDto eweidto = eservice.save(edto);
//		eweidto.setMemberid(edto.getMemberid());
//		eservice.save(eweidto);
//		return "redirect:/user/userinfo?id=" + edto.getMemberid().getUserid().getId();
//	}

	//
	@PostMapping("/admin/member/membertestadd")
	public Map membertestadd(String dummyuserid) {
		boolean flag = true;
		try {
			mservice.dummyMembersave(dummyuserid);
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		return map;

	}

}