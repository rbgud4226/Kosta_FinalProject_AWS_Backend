package com.example.demo.oracledb.depts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.oracledb.members.MembersDto;
import com.example.demo.oracledb.members.MembersService;
import com.example.demo.oracledb.users.Users;

@RestController
@CrossOrigin(origins = "*")
public class DeptsController {

	@Autowired
	private DeptsService dservice;

	@Autowired
	private MembersService mservice;

	@Autowired
	private JoblvsService jservice;

	@GetMapping("/corp/deptlist")
	public Map deptlist() {
		boolean flag = true;
		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
		try {
			dlist = dservice.getAll();
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("dlist", dlist);
		return map;
	}

//	@GetMapping("/admin/corp/deptadd")
//	public String deptaddform() {
//		return "corp/deptadd";
//	}

//	@PostMapping("/admin/corp/deptadd")
//	public String deptadd(DeptsDto dto) {
//		dservice.save(dto);
//		return "redirect:/corp/deptlist";
//	}

	@PostMapping("/admin/corp/deptadd")
	public Map deptadd(DeptsDto dto) {
		System.out.println(dto);
		boolean flag = true;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				dservice.save(dto);
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("message", "부서가 추가되었습니다.");
		return map; // Redirect to department list page
	}

	//
	@GetMapping("/admin/corp/depttestadd")
	public Map depttestadd() {
		boolean flag = true;
		try {
			dservice.dummyDeptsave();
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		return map;
	}

//	@GetMapping("/corp/deptinfo")
//	public String deptinfo(int deptid, ModelMap map) {
//		map.addAttribute("d", dservice.getByDeptId(deptid));
//		return "corp/deptinfo";
//	}

	@ResponseBody
	@GetMapping("/corp/deptinfo")
	public Map deptinfo(int deptid) {
		boolean flag = true;
		Map map = new HashMap();
		try {
			map.put("d", dservice.getByDeptId(deptid));
			map.put("mlist", mservice.getAll());
		} catch (Exception e) {
//			map.put("error", "Failed to fetch dept details.");
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	@PostMapping("/admin/corp/deptedit")
	public Map deptedit(DeptsDto dto) {
		boolean flag = true;
		try {
			dservice.save(dto);
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		return map;
	}

	@PostMapping("/corp/getdeptby")
	public Map getdeptby(String val, int type) {
		boolean flag = true;
		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
		try {
			if (type == 1) {
				System.out.println("val:" + val);
				dlist = dservice.getByDeptNm(val);
				System.out.println("dlist:" + dlist);
			} else if (type == 2) {
				System.out.println("===error1===");
				System.out.println("val:" + val);
				if (!val.isEmpty()) {
					ArrayList<MembersDto> mlist = mservice.getByUsersLike(val);
					for (MembersDto mdto : mlist) {
						ArrayList<DeptsDto> mgriddlist = dservice.getByMgrId(mdto.getMemberid());
						for (DeptsDto ddto : mgriddlist) {
							dlist.add(ddto);
						}
					}
				} else {
					dlist = dservice.getByDeptNm(val);
				}

			}
		} catch (Exception e) {
			System.out.println("===error1===");
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("type", type);
		map.put("val", val);
		map.put("dlist", dlist);
		return map;
	}

	@GetMapping("/admin/corp/deptdel")
	public String deptdel(int deptid) {
		dservice.delDepts(deptid);
		return "redirect:/corp/deptlist";
	}

	@GetMapping("/corp/joblvlist")
	public Map joblvlist() {
		boolean flag = true;
		ArrayList<JoblvsDto> jlist = new ArrayList<JoblvsDto>();
		try {
			jlist = jservice.getAll();
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("jlist", jlist);
		return map;
	}

//	@GetMapping("/admin/corp/joblvadd")
//	public String joblvaddform() {
//		return "corp/joblvadd";
//	}

	//
	@GetMapping("/admin/corp/joblvtestadd")
	public String joblvtestadd() {
		jservice.dummyJoblvsave();
		return "redirect:/corp/joblvlist";
	}

//	@PostMapping("/admin/corp/joblvadd")
//	public String joblvadd(JoblvsDto dto) {
//		jservice.save(dto);
//		return "redirect:/corp/joblvlist";
//	}

	@PostMapping("/admin/corp/joblvadd")
	public Map joblvadd(JoblvsDto dto) {
		System.out.println(dto);
		boolean flag = true;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				System.out.println(jservice.getByJoblvId(dto.getJoblvid()));
				if (jservice.getByJoblvId(dto.getJoblvid()).isEmpty()) {
					jservice.save(dto);
				} else {
					System.out.println("========error1");
					flag = false;
				}
			} else {
				System.out.println("========error2");
				flag = false;
			}
		} catch (Exception e) {
			System.out.println("========error3");
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
//		map.put("message", "직급이 추가되었습니다."); // Add success message to model
		return map;
	}

//	@GetMapping("/corp/joblvinfo")
//	public String joblvinfo(int joblvidx, ModelMap map) {
//		map.addAttribute("j", jservice.getByJoblvIdx(joblvidx));
//		return "corp/joblvinfo";
//	}

	@ResponseBody
	@GetMapping("/corp/joblvinfo")
	public Map joblvinfo(int joblvidx) {
		Map map = new HashMap();
		try {
			map.put("j", jservice.getByJoblvIdx(joblvidx));
		} catch (Exception e) {
//			map.put("error", "Failed to fetch job level details.");
			e.printStackTrace(); // or log the exception
		}
		return map;
	}

	@PostMapping("/admin/corp/joblvedit")
	public String joblvedit(JoblvsDto dto) {
		jservice.save(dto);
		return "redirect:/corp/joblvinfo?joblvidx=" + dto.getJoblvidx();
	}

	@PostMapping("/corp/getjoblvby")
	public Map getjoblvby(String val, int type) {
		boolean flag = true;
		ArrayList<JoblvsDto> jlist = new ArrayList<JoblvsDto>();
		try {
			if (type == 1) {
				if (val == "") {
					jlist = jservice.getByjoblvnmLike(val);
				} else {
					try {
						jlist = jservice.getByJoblvId(Integer.parseInt(val));
					} catch (NumberFormatException var3) {
						jlist = jservice.getByjoblvnmLike(val);
					}
				}

			} else if (type == 2) {
				jlist = jservice.getByjoblvnmLike(val);
			}

		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("type", type);
		map.put("val", val);
		map.put("jlist", jlist);
		return map;
	}

//	@PostMapping("/corp/getdeptby")
//	public Map getdeptby(String val, int type) {
//		boolean flag = true;
//		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
//		try {
//			if (type == 1) {
//				System.out.println("val:" + val);
//				dlist = dservice.getByDeptNm(val);
//				System.out.println("dlist:" + dlist);
//			} else if (type == 2) {
//				System.out.println("===error1===");
//				System.out.println("val:" + val);
//				if (!val.isEmpty()) {
//					ArrayList<MembersDto> mlist = mservice.getByUsersLike(val);
//					for (MembersDto mdto : mlist) {
//						ArrayList<DeptsDto> mgriddlist = dservice.getByMgrId(mdto.getMemberid());
//						for (DeptsDto ddto : mgriddlist) {
//							dlist.add(ddto);
//						}
//					}
//				} else {
//					dlist = dservice.getByDeptNm(val);
//				}
//
//			}
//		} catch (Exception e) {
//			System.out.println("===error1===");
//			flag = false;
//		}
//		Map map = new HashMap();
//		map.put("flag", flag);
//		map.put("type", type);
//		map.put("val", val);
//		map.put("dlist", dlist);
//		return map;
//	}

	@GetMapping("/admin/corp/joblvdel")
	public String joblvdel(int joblvidx) {
		jservice.delJoblvs(joblvidx);
		return "redirect:/corp/joblvlist";
	}

}
