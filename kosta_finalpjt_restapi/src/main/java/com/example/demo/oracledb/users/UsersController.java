package com.example.demo.oracledb.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

//	@Autowired
//	private virtual_users_service virtualservice;

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

//	@GetMapping("/user/userjoin")
//	public String userjoinform(Model model) {
//		model.addAttribute("user", new UsersDto());
//		return "user/userjoin";
//	}

	@PostMapping("/user/userjoin")
	public Map userjoin(@Valid UsersDto dto, Errors errors) {
		boolean flag = true;
		Map map = new HashMap();
		if (errors.hasErrors()) {
			map.put("user", dto);

			Map<String, String> validatorResult = uservice.validateHandling(errors);
			for (String key : validatorResult.keySet()) {
				map.put(key, validatorResult.get(key));
			}
			flag = false;
			map.put("flag", flag);
			return map;
		}
		try {
			dto.setAprov(0);
			uservice.save(dto);
//		virtualservice.save(dto.getId());
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	@GetMapping("/user/useredit/{id}")
	public Map usereditform(@PathVariable("id") String id) {
		boolean flag = true;
		UsersDto udto = new UsersDto();
		String aprovStr = "";
		try {
			udto = uservice.getById(id);
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
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("user", udto);
		map.put("aprovStr", aprovStr);
		return map;
	}

	@PutMapping("/user/useredit")
	public Map useredit(UsersDto udto, String aprovStr) {
		boolean flag = true;
		System.out.println(udto);
		System.out.println(aprovStr);
		try {
			if (aprovStr == "승인대기상태") {
				udto.setAprov(0);
			} else if (aprovStr == "재직상태") {
				udto.setAprov(1);
			} else if (aprovStr == "휴직상태") {
				udto.setAprov(2);
			} else if (aprovStr == "퇴직상태") {
				udto.setAprov(3);
			}
			uservice.update(udto);
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("id", udto.getId());
		return map;
	}

	@PutMapping("/user/userpwdedit")
	public Map userpwdedit(String id, String oldpwd, String newpwd, String confirm_pwd) {
		boolean flag = true;
		Map map = new HashMap();
		if (!uservice.getById(id).getOldpwd().equals(oldpwd)) {
			map.put("oldpwd", oldpwd);
			map.put("newpwd", newpwd);
			map.put("confirm_pwd", confirm_pwd);
			map.put("errormsg", "현재 비밀번호가 일치하지 않습니다.");

			flag = false;
			map.put("flag", flag);
			return map;
		}
		if (!confirm_pwd.equals(newpwd)) {
			map.put("oldpwd", oldpwd);
			map.put("newpwd", newpwd);
			map.put("confirm_pwd", confirm_pwd);
			map.put("errormsg", "새 비밀번호와 일치하지 않습니다.");

			flag = false;
			map.put("flag", flag);
			return map;
		}
		try {
			uservice.updatePwd(id, newpwd);
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	@ResponseBody
	@GetMapping("/admin/user/useraprov")
	public Map useraprov(String id, int aprov) {
		boolean flag = true;
		UsersDto udto = new UsersDto();
		MembersDto mdto = new MembersDto();
		try {
			udto = uservice.getById(id);
			if (mservice.getByuserId(id) != null) {
				mdto = mservice.getByuserId(id);
				udto.setMemberdto(mdto);
				udto.setAprov(aprov);
				uservice.update(udto);
			} else {
				flag = false;
			}

			if (aprov == 3) {
				mdto.setLeavedt(LocalDate.from(LocalDateTime.now()));
				mservice.save(mdto);
			} else {
				mdto.setLeavedt(null);
				mservice.save(mdto);
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("aprov", udto.getAprov());
		return map;
	}

	@GetMapping("/auth/user/userinfo/{id}")
	public Map userinfo(@PathVariable("id") String id) {
//		System.out.println("user:" + uservice.getById(id));
		boolean flag = true;
		UsersDto udto = new UsersDto();
		String aprovStr = "";
		String typeStr = "";
		ArrayList<EduWorkExperienceInfoDto> edulist = new ArrayList<EduWorkExperienceInfoDto>();
		ArrayList<EduWorkExperienceInfoDto> expwoklist = new ArrayList<EduWorkExperienceInfoDto>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]") || id.equals(auth.getName())) {
				udto = uservice.getById(id);
				if (udto.getAprov() == 0) {
					aprovStr = "승인대기상태";
				} else if (udto.getAprov() == 1) {
					aprovStr = "재직상태";
				} else if (udto.getAprov() == 2) {
					aprovStr = "휴직상태";
				} else if (udto.getAprov() == 3) {
					aprovStr = "퇴직상태";
				}

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

				for (EduWorkExperienceInfoDto edto : elist) {
					if (edto.getType() == 0) {
						edulist.add(edto);
					} else {
						expwoklist.add(edto);
					}
				}
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("user", udto);
		map.put("aprovStr", aprovStr);
		map.put("typeStr", typeStr);
		map.put("edulist", edulist);
		map.put("expwoklist", expwoklist);
//		"user/userinfo"
		return map;
	}

	@GetMapping("/admin/user/userlist")
	public Map userlist() {
		boolean flag = true;
		Map map = new HashMap();
		ArrayList<UsersDto> ulist = new ArrayList<UsersDto>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				ulist = uservice.getAll();
				for (UsersDto udto : ulist) {
					mservice.getByuserId(udto.getId());
					udto.setMemberdto(mservice.getByuserId(udto.getId()));
				}
				map.put("ulist", ulist);
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		System.out.println("flag:" + flag);
		map.put("flag", flag);
		return map;
	}

	@PostMapping("/admin/user/getuserby")
	public Map getuserby(String val, int type) {
		boolean flag = true;
		ArrayList<UsersDto> ulist = new ArrayList<UsersDto>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				if (type == 1) {
					ulist = uservice.getByIdLike(val);
					for (UsersDto udto : ulist) {
						MembersDto mdto = mservice.getByuserId(udto.getId());
						try {
							if (mdto.getUserid() == null) {
								udto.setMemberdto(new MembersDto(null, 0, null, null, null, null, null, null, null,
										null, null, null, null, null));
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
//					System.out.println(valInt);
						ulist = uservice.getByAprov(valInt);
						for (UsersDto udto : ulist) {
							MembersDto mdto = mservice.getByuserId(udto.getId());
							try {
								if (mdto.getUserid() == null) {
									udto.setMemberdto(new MembersDto(null, 0, null, null, null, null, null, null, null,
											null, null, null, null, null));
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
				} else {
					flag = false;
				}
//			System.out.println("valint:" + uservice.getByAprov(Integer.parseInt(val)));
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("type", type);
		map.put("val", val);
		map.put("ulist", ulist);
		return map;
	}

	//
	@GetMapping("/admin/user/usertestadd")
	public Map usertestadd() {
		boolean flag = true;
		try {
			uservice.dummyUsersave();
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		return map;
	}

	// 채팅 유저 목록
	@GetMapping("/user/list")
	public String del(ModelMap map) {
		map.addAttribute("list", uservice.getAll());
		return "member/list";
	}

}
