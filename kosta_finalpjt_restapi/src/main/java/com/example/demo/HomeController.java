package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.oracledb.auth.MyTokenProvider;
import com.example.demo.oracledb.charts.ChartsService;
import com.example.demo.oracledb.depts.DeptsService;
import com.example.demo.oracledb.members.MembersDto;
import com.example.demo.oracledb.members.MembersService;
import com.example.demo.oracledb.users.UsersDto;
import com.example.demo.oracledb.users.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "*")
//@Controller
public class HomeController {
	@Autowired
	private UsersService uservice;

	@Autowired
	private MembersService mservice;

	@Autowired
	private DeptsService dservice;

	@Autowired
	private ChartsService chartsService;

	//
	//
	//
	@Autowired
	private AuthenticationManagerBuilder ambuilder;

	@Autowired
	private MyTokenProvider myTokenProvider;

	@GetMapping("/")
	public String home() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		String type = myTokenProvider.getRoles(id);
		String indexPath = "";
		if (id == null) {
			indexPath = "user/userlogin";
		} else {
			if (type == "admin") {
				indexPath = "/index_admin";
			} else if (type == "emp") {
				indexPath = "/index_emp";
			} else {
				indexPath = "/index";
			}
		}
		return indexPath;
	}

	@GetMapping("/loginform")
	public String loginform() {
		return "user/userlogin";
	}

	@PostMapping("/loginerror")
	public String loginerror(HttpServletRequest request, String loginFailMsg) {
		return "user/userlogin";
	}

	@GetMapping("/auth/login")
	public String authlogin() {
		return "/loginform";
	}

	@GetMapping("/auth/logout")
	public String authlogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/index_admin")
	public Map adminHome() {
		Map map = new HashMap();
		boolean flag = true;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			String id = auth.getName();
			MembersDto mdto = mservice.getByuserId(id);
//			map.put("usernm", mdto.getUserid().getUsernm());
			if (mdto != null) {
				if (mdto.getMemberimgnm() == "") {
					map.put("memberimgnm", "");
				} else {
					map.put("memberimgnm", mdto.getMemberimgnm());
				}
				if (mdto.getDeptid() == null) {
					map.put("deptnm", "미등록 상태");
				} else {
					map.put("deptnm", mdto.getDeptid().getDeptnm());
					if (mdto.getDeptid().getMgrid() != null
							&& mdto.getDeptid().getMgrid().getMemberid() == mdto.getMemberid()) {
						map.put("mgr_deptid", mdto.getDeptid().getDeptid());
					}
				}
				if (mdto.getJoblvid() == null) {
					map.put("joblvnm", "미등록 상태");
				} else {
					map.put("joblvnm", mdto.getJoblvid().getJoblvnm());
				}
			}
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	@GetMapping("/index_emp")
	public Map empHome() {
		Map map = new HashMap();
		boolean flag = true;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			String id = auth.getName();
			UsersDto udto = uservice.getById(id);
			MembersDto mdto = mservice.getByuserId(udto.getId());
//			map.put("usernm", mdto.getUserid().getUsernm());
			if (mdto != null) {
				if (mdto.getMemberimgnm() == "") {
					map.put("memberimgnm", "");
				} else {
					map.put("memberimgnm", mdto.getMemberimgnm());
				}
				if (mdto.getDeptid() == null) {
					map.put("deptnm", "미등록 상태");
				} else {
					map.put("deptnm", mdto.getDeptid().getDeptnm());
					if (mdto.getDeptid().getMgrid() != null
							&& mdto.getDeptid().getMgrid().getMemberid() == mdto.getMemberid()) {
						map.put("mgr_deptid", mdto.getDeptid().getDeptid());
					}
				}
				if (mdto.getJoblvid() == null) {
					map.put("joblvnm", "미등록 상태");
				} else {
					map.put("joblvnm", mdto.getJoblvid().getJoblvnm());
				}
			}
			map.put("list", chartsService.getbyUsers(id));
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	//
	//
	//
	@ResponseBody
	@PostMapping("/login")
	public Map login(String id, String pwd) {
		System.out.println("login 컨트롤");
		// 인증에 사용할 객체. username / password 를 비교하여 인증하는 클래스
		UsernamePasswordAuthenticationToken upauthtoken = new UsernamePasswordAuthenticationToken(id, pwd);
		System.out.println("upauthtoken:" + upauthtoken);
		// authentication() 인증 메서드. 인증한 결과를 Authentication에 담아 반환>>인증하고 인증한 결과를 반환
		Authentication auth = ambuilder.getObject().authenticate(upauthtoken);
		System.out.println("auth:" + auth);

		// isAuthenticated(): 인증결과 반환(true/false)
		boolean flag = auth.isAuthenticated();
		System.out.println("인증결과:" + flag);

		String errorMessage = "";
		Map map = new HashMap();
		if (flag) {
			String token = myTokenProvider.getToken(uservice.getById(id)); // >>토큰 생성시 dto 필요
			String type = myTokenProvider.getRoles(token);

			MembersDto mdto = mservice.getByuserId(id);
//			map.put("usernm", mdto.getUserid().getUsernm());
			if (mdto != null) {
				if (mdto.getMemberid() == 0) {
					map.put("memberid", "");
				} else {
					map.put("memberid", mdto.getMemberid());
				}
				if (mdto.getMemberimgnm() == "") {
					map.put("memberimgnm", "");
				} else {
					map.put("memberimgnm", mdto.getMemberimgnm());
				}
				if (mdto.getDeptid() == null) {
					map.put("deptnm", "미등록 상태");
				} else {
					map.put("deptnm", mdto.getDeptid().getDeptnm());
					if (mdto.getDeptid().getMgrid() != null
							&& mdto.getDeptid().getMgrid().getMemberid() == mdto.getMemberid()) {
						map.put("mgr_deptid", mdto.getDeptid().getDeptid());
					}
				}
				if (mdto.getJoblvid() == null) {
					map.put("joblvnm", "미등록 상태");
				} else {
					map.put("joblvnm", mdto.getJoblvid().getJoblvnm());
				}
			}

			map.put("id", id);
			map.put("token", token);
			map.put("type", type);
			map.put("usernm", uservice.getById(id).getUsernm());
			map.put("aprov", uservice.getById(id).getAprov());
			System.out.println(token);
			System.out.println(id);
			System.out.println(type);
			System.out.println("로그인");
		} else {
			if (uservice.getById(id) == null) {
				errorMessage = "계정을 찾을 수 없습니다.";
			} else if (!uservice.getById(id).getOldpwd().equals(pwd)) {
				errorMessage = "잘못된 비밀번호입니다.";
			}
			map.put("id", id);
			map.put("pwd", pwd);
			System.out.println("로그인 실패");
		}
		map.put("errorMessage", errorMessage);
		map.put("flag", flag);
		return map;
	}

}
