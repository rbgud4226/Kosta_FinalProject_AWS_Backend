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
	public ModelMap memberlist(ModelMap map) {
		ArrayList<MembersDto> mlist = mservice.getAll();
		return map.addAttribute("mlist", mlist);
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
		ArrayList<MembersDto> mlist = new ArrayList<MembersDto>();
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
					if (udto != null && mservice.getByuserNm(new Users(udto.getId(), "", "", "", 0, null)) != null) {
						mlist.add(mservice.getByuserNm(new Users(udto.getId(), "", "", "", 0, null)));
					}
				}
			} else if (type == 3) {
				mlist = mservice.getByJobLvLike(val);
			}
		} else {
			mlist = null;
		}
		System.out.println(mlist);
//		ModelAndView mav = new ModelAndView("member/memberlist");
//		mav.addObject("type", type);
//		mav.addObject("val", val);
		Map map = new HashMap<>();
		map.put("mlist", mlist);
		return map;
	}

	@GetMapping("/member/memberinfo")
	public String memberinfo(String id, ModelMap map) {
		UsersDto udto = uservice.getById(id);
		MembersDto mdto = mservice.getByuserId(id);
		
		ArrayList<EduWorkExperienceInfoDto> elist = new ArrayList<EduWorkExperienceInfoDto>();
		if (mservice.getByuserId(id) != null) {
			elist = eservice.getByMembers(mservice.getByuserId(id).getMemberid());
		}
		ArrayList<EduWorkExperienceInfoDto> edulist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<EduWorkExperienceInfoDto> expwoklist = new ArrayList<EduWorkExperienceInfoDto>();
		for (EduWorkExperienceInfoDto edto : elist) {
			if (edto.getType() == 0) {
				edulist.add(edto);
			} else {
				expwoklist.add(edto);
			}
		}
		map.addAttribute("user", udto);
		map.addAttribute("mdto", mdto);
		map.addAttribute("edulist", edulist);
		map.addAttribute("expwoklist", expwoklist); 
		return "member/memberinfo";
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
	public String membereditform(String id, ModelMap map) {
		MembersDto mdto = mservice.getByuserId(id);
		map.addAttribute("member", mdto);
		map.addAttribute("userid", id);
		ArrayList<DeptsDto> dlistAll = dservice.getAll();
		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
		for (DeptsDto ddto : dlistAll) {
			if (ddto.getMgrid() != null && ddto.getMgrid().getMemberid() == mservice
					.getByMemberId(ddto.getMgrid().getMemberid()).getMemberid()) {
				mdto = mservice.getByMemberId(ddto.getMgrid().getMemberid());
				ddto.setMgrid(new Members(mdto.getUserid(), mdto.getMemberid(), mdto.getBirthdt(), mdto.getEmail(),
						mdto.getCpnum(), mdto.getAddress(), mdto.getMemberimgnm(), mdto.getHiredt(), mdto.getLeavedt(),
						mdto.getDeptid(), mdto.getJoblvid(), mdto.getMgrid(), null));
				dlist.add(ddto);
			}
		}
		map.addAttribute("dlist", dlist);
		map.addAttribute("jlist", jservice.getAll());
		return "member/memberedit";
	}

	@PostMapping("/member/memberadd")
	public String memberadd(MembersDto dto, EduWorkExperienceInfoDto edto) {
		MembersDto mdto = mservice.save(dto);
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
			eweidto.setMemberid(new Members(null, mdto.getMemberid(), null, null, null, null, null, null, null, null, null, null, null));
			eservice.save(eweidto);
		}
		return "redirect:/member/memberinfo?id=" + dto.getUserid().getId();
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
	public String membertestadd(String dummyuserid) {
		mservice.dummyMembersave(dummyuserid);
		return "redirect:/admin/user/userlist";

	}


}