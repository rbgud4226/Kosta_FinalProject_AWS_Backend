package com.example.demo.oracledb.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.mariadb.users.virtual_users_service;
import com.example.demo.oracledb.members.EduWorkExperienceInfoDto;
import com.example.demo.oracledb.members.EduWorkExperienceInfoService;
import com.example.demo.oracledb.members.MembersDto;
import com.example.demo.oracledb.members.MembersService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
//@RequiredArgsConstructor
//@Controller
public class UsersController {
	@Autowired
	private UsersService uservice;

	@Autowired
	private MembersService mservice;

	@Autowired
	private EduWorkExperienceInfoService eservice;

	@Autowired
	private virtual_users_service virtualservice;

	@Autowired
	private UsersCheckIdValidator usersCheckIdValidator;

//	@Autowired
//	private UsersCheckUsernmValidator usersCheckUsernmValidator;

	@Autowired
	private UsersEqualPwdValidator usersEqualPwdValidator;

	@InitBinder
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(usersCheckIdValidator);
//		binder.addValidators(usersCheckUsernmValidator);
		binder.addValidators(usersEqualPwdValidator);

	}

	@GetMapping("/user/userjoin")
	public String userjoinform(Model model) {
		model.addAttribute("user", new UsersDto());
		return "user/userjoin";
	}

	@PostMapping("/user/userjoin")
	public String userjoin(@Valid UsersDto dto, Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("user", dto);

			Map<String, String> validatorResult = uservice.validateHandling(errors);
			for (String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
				System.out.println(validatorResult.get(key));
			}
			return "/user/userjoin";
		}
		System.out.println(dto);
		dto.setAprov(0);
		uservice.save(dto);
		virtualservice.save(dto.getId());
		return "redirect:/";
	}

	@GetMapping("/user/useredit")
	public String usereditform(String id, ModelMap map) {
		UsersDto udto = uservice.getById(id);
		String aprovStr = "";
		if (udto.getAprov() == 0) {
			aprovStr = "승인대기상태";
		} else if (udto.getAprov() == 1) {
			aprovStr = "재직상태";
		} else if (udto.getAprov() == 2) {
			aprovStr = "휴직상태";
		} else if (udto.getAprov() == 3) {
			aprovStr = "퇴직상태";
		}
		udto.setMemberdto(mservice.getByuserId(udto.getId()));
		map.addAttribute("user", udto);
		map.addAttribute("aprovStr", aprovStr);
		return "user/useredit";
	}

	@PostMapping("/user/useredit")
	public String useredit(UsersDto udto, String aprovStr) {
		if (aprovStr == "승인대기상태") {
			udto.setAprov(0);
		} else if (aprovStr == "재직상태") {
			udto.setAprov(1);
		} else if (aprovStr == "휴직상태") {
			udto.setAprov(2);
		} else if (aprovStr == "퇴직상태") {
			udto.setAprov(3);
		}
		uservice.updatePwd(udto);
		return "redirect:/user/userinfo?id=" + udto.getId();
	}

	@PostMapping("/user/userpwdedit")
	public String userpwdedit(UsersDto udto, String aprovStr) {
		if (aprovStr == "승인대기상태") {
			udto.setAprov(0);
		} else if (aprovStr == "재직상태") {
			udto.setAprov(1);
		} else if (aprovStr == "휴직상태") {
			udto.setAprov(2);
		} else if (aprovStr == "퇴직상태") {
			udto.setAprov(3);
		}
		uservice.updatePwd(udto);
		return "redirect:/user/userinfo?id=" + udto.getId();
	}

	@ResponseBody
	@GetMapping("/admin/user/useraprov")
	public Map useraprov(String id, int aprov) {
		Map map = new HashMap();
		UsersDto udto = uservice.getById(id);
		MembersDto mdto = mservice.getByuserId(id);
		udto.setMemberdto(mdto);
		udto.setAprov(aprov);
		uservice.update(udto);

		if (aprov == 3) {
			mdto.setLeavedt(LocalDate.from(LocalDateTime.now()));
			mservice.save(mdto);
		} else {
			mdto.setLeavedt(null);
			mservice.save(mdto);
		}
		map.put("aprov", udto.getAprov());
		return map;
	}

//	@GetMapping("/user/userinfo")
//	public String myinfo(String id, ModelMap map) {
////		System.out.println("user:" + uservice.getById(id));
//		UsersDto udto = uservice.getById(id);
//		String aprovStr = "";
//		if (udto.getAprov() == 0) {
//			aprovStr = "승인대기상태";
//		} else if (udto.getAprov() == 1) {
//			aprovStr = "재직상태";
//		} else if (udto.getAprov() == 2) {
//			aprovStr = "휴직상태";
//		} else if (udto.getAprov() == 3) {
//			aprovStr = "퇴직상태";
//		}
//		
//		String typeStr = "";
//		if (udto.getType().equals("admin")) {
//			typeStr = "관리자";
//		} else if (udto.getType().equals("emp")) {
//			typeStr = "직원";
//		}
//		udto.setMemberdto(mservice.getByuserId(udto.getId()));
//		ArrayList<EduWorkExperienceInfoDto> elist = new ArrayList<EduWorkExperienceInfoDto>();
//		if (mservice.getByuserId(id) != null) {
//			elist = eservice.getByMembers(mservice.getByuserId(id).getMemberid());
//		}
//		ArrayList<EduWorkExperienceInfoDto> edulist = new ArrayList<EduWorkExperienceInfoDto>();
//		ArrayList<EduWorkExperienceInfoDto> expwoklist = new ArrayList<EduWorkExperienceInfoDto>();
//		for (EduWorkExperienceInfoDto edto : elist) {
//			if (edto.getType() == 0) {
//				edulist.add(edto);
//			} else {
//				expwoklist.add(edto);
//			}
//		}
//		map.addAttribute("user", udto);
//		map.addAttribute("aprovStr", aprovStr);
//		map.addAttribute("typeStr", typeStr);
//		map.addAttribute("edulist", edulist);
//		map.addAttribute("expwoklist", expwoklist);
//		return "user/userinfo";
//	}

	@GetMapping("/auth/user/userinfo")
	public Map myinfo() {
//		System.out.println("user:" + uservice.getById(id));
		Map map = new HashMap();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		UsersDto udto = uservice.getById(id);
		String aprovStr = "";
		if (udto.getAprov() == 0) {
			aprovStr = "승인대기상태";
		} else if (udto.getAprov() == 1) {
			aprovStr = "재직상태";
		} else if (udto.getAprov() == 2) {
			aprovStr = "휴직상태";
		} else if (udto.getAprov() == 3) {
			aprovStr = "퇴직상태";
		}

		String typeStr = "";
		if (udto.getType().equals("admin")) {
			typeStr = "관리자";
		} else if (udto.getType().equals("emp")) {
			typeStr = "직원";
		}
		udto.setMemberdto(mservice.getByuserId(udto.getId()));
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
		map.put("user", udto);
		map.put("aprovStr", aprovStr);
		map.put("typeStr", typeStr);
		map.put("edulist", edulist);
		map.put("expwoklist", expwoklist);
//		"user/userinfo"
		return map;
	}

	@GetMapping("/admin/user/userlist")
	public String userlist(ModelMap map) {
		ArrayList<UsersDto> ulist = uservice.getAll();
		for (UsersDto udto : ulist) {
			mservice.getByuserId(udto.getId());
			udto.setMemberdto(mservice.getByuserId(udto.getId()));
		}
//		System.out.println("ulist:" + ulist);
		map.addAttribute("ulist", ulist);
		return "user/userlist";
	}

	@PostMapping("/admin/user/getdeptby")
	public ModelAndView getuserby(String val, int type) {
		ArrayList<UsersDto> ulist = null;
		if (type == 1) {
			ulist = uservice.getByIdLike(val);
			for (UsersDto udto : ulist) {
				MembersDto mdto = mservice.getByuserId(udto.getId());
				try {
					if (mdto.getUserid() == null) {
						udto.setMemberdto(new MembersDto(null, 0, null, null, null, null, null, null, null, null, null,
								null, null, null));
					} else if (mdto.getUserid() != null && udto.getId() == mdto.getUserid().getId()) {
						udto.setMemberdto(mdto);
					}
				} catch (NullPointerException e) {
//					e.printStackTrace();
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		} else if (type == 2) {
			ulist = new ArrayList<UsersDto>();
			for (UsersDto udto : uservice.getAll()) {
				if (val != "") {
					ArrayList<MembersDto> mlist = mservice.getByDeptNmLike(val);
					System.out.println("mlist:" + mlist);
					for (MembersDto mdto : mlist) {
						if (udto.getMemberdto() != null && udto.getId() == mdto.getUserid().getId()) {
							udto.setMemberdto(mdto);
							ulist.add(udto);
						}
					}
				} else {
					ulist = null;
				}
			}
		} else if (type == 3) {
			ulist = new ArrayList<UsersDto>();
			for (UsersDto udto : uservice.getByUsernmLike(val)) {
				if (val != "") {
					MembersDto mdto = mservice.getByuserId(udto.getId());
					udto.setMemberdto(mdto);
					ulist.add(udto);
				} else {
					ulist = null;
				}
			}
		} else if (type == 4) {
			ulist = new ArrayList<UsersDto>();
			for (UsersDto udto : uservice.getAll()) {
				if (val != "") {
					ArrayList<MembersDto> mlist = mservice.getByJobLvLike(val);
					for (MembersDto mdto : mlist) {
						if (udto.getMemberdto() != null && udto.getId() == mdto.getUserid().getId()) {
							udto.setMemberdto(mdto);
							ulist.add(udto);
						}
					}
				} else {
					ulist = null;
				}
			}
		} else if (type == 5) {
			int valInt = 4;
			if (val != "") {
				if (val.contains("미") || val.contains("대") || val.contains("미승인")) {
					valInt = 0;
				} else if (val.contains("재") || val.contains("승인")) {
					valInt = 1;
				} else if (val.contains("휴")) {
					valInt = 2;
				} else if (val.contains("퇴")) {
					valInt = 3;
				} else {
					valInt = 5;
				}
				System.out.println(valInt);
				ulist = uservice.getByAprov(valInt);
				for (UsersDto udto : ulist) {
					MembersDto mdto = mservice.getByuserId(udto.getId());
					try {
						if (mdto.getUserid() == null) {
							udto.setMemberdto(new MembersDto(null, 0, null, null, null, null, null, null, null, null,
									null, null, null, null));
						} else if (mdto.getUserid() != null && udto.getId() == mdto.getUserid().getId()) {
							udto.setMemberdto(mdto);
						}
					} catch (NullPointerException e) {
//					e.printStackTrace();
					} catch (Exception e) {
//					e.printStackTrace();
					}
				}
			} else {
				ulist = null;
			}
//			System.out.println("valint:" + uservice.getByAprov(Integer.parseInt(val)));
		}
		System.out.println("ulist:" + ulist);
		ModelAndView mav = new ModelAndView("user/userlist");
		mav.addObject("type", type);
		mav.addObject("val", val);
		mav.addObject("ulist", ulist);
		return mav;
	}

	//
	@GetMapping("/admin/user/usertestadd")
	public String usertestadd() {
		uservice.dummyUsersave();
		return "redirect:/admin/user/userlist";
	}

	// 채팅 유저 목록
	@GetMapping("/user/list")
	public String del(ModelMap map) {
		map.addAttribute("list", uservice.getAll());
		return "member/list";
	}

}
