package com.example.demo.oracledb.members;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.oracledb.depts.DeptsDto;
import com.example.demo.oracledb.depts.DeptsService;
import com.example.demo.oracledb.depts.JoblvsDto;
import com.example.demo.oracledb.depts.JoblvsService;
import com.example.demo.oracledb.users.Users;
import com.example.demo.oracledb.users.UsersDto;
import com.example.demo.oracledb.users.UsersService;

@RestController
@CrossOrigin(origins = "*")
//@Controller
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

//	private String dirName = "/src/main/resources/static/img/member/";

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
						if (udto != null && mservice.getByuserNm(new Users(udto.getId(), "", "", "", 0, null)) != null
								&& mservice.getByuserNm(new Users(udto.getId(), "", "", "", 0, null)).getUserid()
										.getAprov() == 1) {
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
	
	@ResponseBody
	@GetMapping("/member/memberinfo/{id}")
	public Map memberinfo(@PathVariable("id") String id) {
		boolean flag = true;
		UsersDto udto = new UsersDto();
		MembersDto mdto = new MembersDto();
		Map map = new HashMap<>();

		ArrayList<EduWorkExperienceInfoDto> elist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<EduWorkExperienceInfoDto> edulist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<EduWorkExperienceInfoDto> expwoklist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]") || id.equals(auth.getName())) {
				udto = uservice.getById(id);
				mdto = mservice.getByuserId(id);
				map.put("user", udto);
				map.put("mdto", mdto);
				System.out.println("=====error1");
				if (mservice.getByuserId(id) != null) {
					elist = eservice.getByMembers(mservice.getByuserId(id).getMemberid());
				}
				for (EduWorkExperienceInfoDto edto : elist) {
					System.out.println("=====error2");
					if (edto.getType() == 0) {
						edulist.add(edto);
					} else {
						expwoklist.add(edto);
					}
				}
				map.put("edulist", edulist);
				map.put("expwoklist", expwoklist);
				ArrayList<DeptsDto> dlistAll = dservice.getAll();
				for (DeptsDto ddto : dlistAll) {
					System.out.println("=====error3");
					if (ddto.getMgrid() != null && ddto.getMgrid().getMemberid() == mservice
							.getByMemberId(ddto.getMgrid().getMemberid()).getMemberid()) {
						mdto = mservice.getByMemberId(ddto.getMgrid().getMemberid());
						ddto.setMgrid(
								new Members(mdto.getUserid(), mdto.getMemberid(), mdto.getBirthdt(), mdto.getEmail(),
										mdto.getCpnum(), mdto.getAddress(), mdto.getMemberimgnm(), mdto.getHiredt(),
										mdto.getLeavedt(), mdto.getDeptid(), mdto.getJoblvid(), mdto.getMgrid(), null));
						dlist.add(ddto);
					}
				}
				map.put("dlist", dlist);
				map.put("jlist", jservice.getAll());
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		System.out.println("=====error4");
		return map;
	}
//	@GetMapping("/member/memberedit")
//	public Map membereditform(String id) {
//		boolean flag = true;
//		MembersDto mdto = new MembersDto();
//		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
//		try {
//			mdto = mservice.getByuserId(id);
//			ArrayList<DeptsDto> dlistAll = dservice.getAll();
//			for (DeptsDto ddto : dlistAll) {
//				if (ddto.getMgrid() != null && ddto.getMgrid().getMemberid() == mservice
//						.getByMemberId(ddto.getMgrid().getMemberid()).getMemberid()) {
//					mdto = mservice.getByMemberId(ddto.getMgrid().getMemberid());
//					ddto.setMgrid(new Members(mdto.getUserid(), mdto.getMemberid(), mdto.getBirthdt(), mdto.getEmail(),
//							mdto.getCpnum(), mdto.getAddress(), mdto.getMemberimgnm(), mdto.getHiredt(),
//							mdto.getLeavedt(), mdto.getDeptid(), mdto.getJoblvid(), mdto.getMgrid(), null));
//					dlist.add(ddto);
//				}
//			}
//		} catch (Exception e) {
//			flag = false;
//		}
//		Map map = new HashMap<>();
//		map.put("flag", flag);
//		map.put("member", mdto);
//		map.put("userid", id);
//		map.put("dlist", dlist);
//		map.put("jlist", jservice.getAll());
//		return map;
//	}

	@PostMapping("/member/memberchatinfo")
	@ResponseBody
	public Map<String, Object> memberchatinfo(@RequestParam(name = "userid") String userid) {
		System.out.println("요청 들어오나요 ???");
		MembersDto mdto = mservice.getByuserId(userid);
		Users username = uservice.getById2(userid);
//		DeptsDto deptN = dservice.getByDeptId(mdto.getDeptid().getDeptid());
		JoblvsDto jobL = jservice.getByJoblvIdx(mdto.getJoblvid().getJoblvidx());
		Map<String, Object> memchatinfo = new HashMap<>();
//		memchatinfo.put("deptN", deptN);
		memchatinfo.put("jobL", jobL);
		memchatinfo.put("member", mdto);
		memchatinfo.put("membername", username.getUsernm());
		return memchatinfo;
	}

	@PostMapping("/member/memberchatinfo/membername")
	@ResponseBody
	public Map memberchatinfomembername(@RequestParam("userid[]") List<String> userid) {
		Map map = new HashMap();
		ArrayList<String> list = new ArrayList<>();
		for (String s : userid) {
			list.add(uservice.getById2(s).getUsernm());
		}
		map.put("namelist", list);
		return map;
	}

	@GetMapping("/member/memberimg/{memberimgnm}")
	public ResponseEntity<byte[]> read_img(@PathVariable("memberimgnm") String memberimgnm) {
		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		if (memberimgnm.equals("") || memberimgnm.equals("undefined") || memberimgnm.equals("null")
				|| memberimgnm == null) {
			try {
				String defaultfpath = "file:" + path;
				Resource resource = resourceLoader.getResource(defaultfpath + "human.png");
//				InputStream istream = resourceLoader.getResource("classpath:/static/img/common/human.png")
//				InputStream istream = resourceLoader.getResource(path + "human.png")
				InputStream istream = resource.getInputStream();
				header.setContentType(MediaType.IMAGE_PNG);
				result = new ResponseEntity<byte[]>(istream.readAllBytes(), header, HttpStatus.OK);
				istream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
//			File f = new File(path + dirName + memberimgnm);
			File f = new File(path + memberimgnm);
//			Path pathf = Paths.get(path + dirName + memberimgnm);
			Path pathf = Paths.get(path + memberimgnm);
			try {
				header.add("Content-Type", Files.probeContentType(f.toPath()));
				if (Files.exists(pathf)) {
					result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
				} else {
					InputStream istream = resourceLoader.getResource(path + "human.png")
							.getInputStream();
					header.setContentType(MediaType.IMAGE_PNG);
					result = new ResponseEntity<byte[]>(istream.readAllBytes(), header, HttpStatus.OK);
				}
			} catch (NoSuchFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
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
			System.out.println("dto:" + dto);
			if (dto.getHiredt() != null) {
				mdto.setHiredt(dto.getHiredt());
			}
			if (dto.getLeavedt() != null) {
				mdto.setLeavedt(dto.getLeavedt());
			}
			mdto = mservice.save(dto);
			if (!dto.getMemberimgf().isEmpty()) {
				String oname = dto.getMemberimgf().getOriginalFilename();
				String f1 = oname.substring(oname.lastIndexOf("."));
				String f2 = oname.substring(oname.lastIndexOf(".") + 1, oname.length());
				String f3 = oname.substring(0, oname.lastIndexOf("."));
				String fname = f3 + " (" + mdto.getUserid().getUsernm() + ")." + f2;
//				File newFile = new File(path + dirName + fname);
				File newFile = new File(path + fname);
				try {
					dto.getMemberimgf().transferTo(newFile);
					mdto.setMemberimgnm(newFile.getName());
					mservice.save(mdto);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				;
				System.out.println("edto:" + edto);
				EduWorkExperienceInfoDto eweidto = new EduWorkExperienceInfoDto();
				eweidto.setMemberid(new Members(null, mdto.getMemberid(), null, null, null, null, null, null, null,
						null, null, null, null));
				eweidto = eservice.save(edto);
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
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