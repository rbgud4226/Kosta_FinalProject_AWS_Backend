package com.example.demo.oracledb.depts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.oracledb.members.MembersDto;
import com.example.demo.oracledb.members.MembersService;

@Controller
public class DeptsController {

	@Autowired
	private DeptsService dservice;

	@Autowired
	private MembersService mservice;

	@Autowired
	private JoblvsService jservice;

	@GetMapping("/corp/deptlist")
	public String deptlist(ModelMap map) {
		ArrayList<DeptsDto> dlist = dservice.getAll();
		map.addAttribute("dlist", dlist);
//		System.out.println(dlist.get(0).getMgrid().getMemberid());
//		System.out.println(dlist.get(0).getMgrid().getUserid());
		return "corp/deptlist";
	}

	@GetMapping("/admin/corp/deptadd")
	public String deptaddform() {
		return "corp/deptadd";
	}

//	@PostMapping("/admin/corp/deptadd")
//	public String deptadd(DeptsDto dto) {
//		dservice.save(dto);
//		return "redirect:/corp/deptlist";
//	}

	@PostMapping("/admin/corp/deptadd")
	public String deptadd(DeptsDto dto, Model model) {
		// Process and save the department using your service
		dservice.save(dto); // Assuming dservice is your service class

		model.addAttribute("message", "부서가 추가되었습니다."); // Add success message to model

		// Return the view name where you want to redirect after successful addition
		return "redirect:/corp/deptlist"; // Redirect to department list page
	}

	//
	@GetMapping("/admin/corp/depttestadd")
	public String depttestadd() {
		dservice.dummyDeptsave();
		return "redirect:/corp/deptlist";
	}

//	@GetMapping("/corp/deptinfo")
//	public String deptinfo(int deptid, ModelMap map) {
//		map.addAttribute("d", dservice.getByDeptId(deptid));
//		return "corp/deptinfo";
//	}

	@ResponseBody
	@GetMapping("/corp/deptinfo")
	public Map deptinfo(int deptid) {
		Map map = new HashMap();
		try {
			map.put("d", dservice.getByDeptId(deptid));
			map.put("mlist", mservice.getAll());
		} catch (Exception e) {
//			map.put("error", "Failed to fetch dept details.");
			e.printStackTrace(); // or log the exception
		}
		return map;
	}

	@PostMapping("/admin/corp/deptedit")
	public String deptedit(DeptsDto dto) {
		dservice.save(dto);
		return "redirect:/corp/deptinfo?deptid=" + dto.getDeptid();
	}

	@PostMapping("/corp/getdeptby")
	public ModelAndView getdeptby(String val, int type) {
		ArrayList<DeptsDto> dlist = new ArrayList<DeptsDto>();
		if (type == 1) {
			dlist = dservice.getByDeptNm(val);
		} else if (type == 2) {
			MembersDto mdto = mservice.getByuserId(val);
			dlist = dservice.getByMgrId(mdto.getMemberid());
		}
		ModelAndView mav = new ModelAndView("corp/deptlist");
		mav.addObject("type", type);
		mav.addObject("val", val);
		mav.addObject("dlist", dlist);
		return mav;
	}
	
	@GetMapping("/admin/corp/deptdel")
	public String deptdel(int deptid) {
		dservice.delDepts(deptid);
		return "redirect:/corp/deptlist";
	}

	@GetMapping("/corp/joblvlist")
	public String joblvlist(ModelMap map) {
		ArrayList<JoblvsDto> jlist = jservice.getAll();
		map.addAttribute("jlist", jlist);
//		System.out.println(jlist.get(0).getJoblvidx());
		return "corp/joblvlist";
	}

	@GetMapping("/admin/corp/joblvadd")
	public String joblvaddform() {
		return "corp/joblvadd";
	}

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
	public String joblvadd(JoblvsDto dto, Model model) {
		// Process and save the department using your service
		jservice.save(dto); // Assuming dservice is your service class

		model.addAttribute("message", "직급이 추가되었습니다."); // Add success message to model
		System.out.println(dto.getJoblvid());
		System.out.println(dto.getJoblvnm());
		// Return the view name where you want to redirect after successful addition
		return "redirect:/corp/joblvlist"; // Redirect to department list page
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
	public ModelAndView getjoblvby(String val, int type) {
		ArrayList<JoblvsDto> jlist = new ArrayList<JoblvsDto>();
		if (type == 1) {
			jlist = jservice.getByJoblvId(Integer.parseInt(val));
		} else if (type == 2) {
			jlist = jservice.getByjoblvnmLike(val);
		}
		ModelAndView mav = new ModelAndView("corp/joblvlist");
		mav.addObject("type", type);
		mav.addObject("val", val);
		mav.addObject("jlist", jlist);
		return mav;
	}
	
	@GetMapping("/admin/corp/joblvdel")
	public String joblvdel(int joblvidx) {
		jservice.delJoblvs(joblvidx);
		return "redirect:/corp/joblvlist";
	}

}
