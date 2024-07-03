package com.example.demo.oracledb.docx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth/docx")
public class DocxController {

	@Autowired
	private DocxService service;

	@GetMapping("/add")
	public String addForm() {
		return "docx/list";
	}

	// 작성폼 불러올때 멤버 리스트 데이터 가져오기
	@GetMapping("/addreport")
	public String reportForm(ModelMap map) {
		map.addAttribute("mlist", service.getMemAll());

		return "docx/addreport";
	}

	// 보고서 작성양식
	@PostMapping("/addreport")
	public String addreport(DocxDto d) {
		String senior = d.getSenior();
		if (senior != null) {
			int dkey = service.findByFormtypeDesc(d.getFormtype());
			dkey += 1;
			String[] s = d.getSenior().split(",");
			for (int j = 0; j < s.length; j++) {
				service.save(d, s[j], null, j, dkey);
			}
		}
		return "redirect:/auth/docx/list";
	}

	// 휴가서류 작성양식
	@PostMapping("/addvacation")
	public String addvacation(DocxDto d) {
		int dkey = service.findByFormtypeDesc(d.getFormtype());
		dkey += 1;
		System.out.println("key값 있는지 확인 ::" + dkey);
		String[] s = d.getSenior().split(",");
		for (int j = 0; j < s.length; j++) {
			service.save(d, s[j], null, j, dkey);
		}
		return "redirect:/auth/docx/list";
	}

	// 회의록 작성양식
	@PostMapping("/addmeeting")
	public String addmeeting(DocxDto d) {
		String senior = d.getSenior();
		int dkey = service.findByFormtypeDesc(d.getFormtype());
		dkey += 1;
		if (senior != null) {
			String[] s = d.getSenior().split(",");
			for (int j = 0; j < s.length; j++) {
				service.save(d, null, d.getParticipant(), j, dkey);
			}
		}
		return "redirect:/auth/docx/list";
	}

	// 전체문서 리스트 출력
	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size,
			ModelMap map) {
		List<DocxDto> docxList = service.getAllByDocxorderWithPagination(page, size);
		int totalCount = service.getTotalCountByDocxorder();
		int totalPage = (int) Math.ceil((double) totalCount / size);
		map.addAttribute("list", docxList);
		map.addAttribute("currentPage", page);
		map.addAttribute("totalPages", totalPage);
		map.addAttribute("pageSize", size);
		return "docx/list";
	}

	// 결재걸린 문서 리스트 출력
	@GetMapping("/slist")
	public String slist(ModelMap map, HttpSession session) {
		String loginId = (String) session.getAttribute("loginId");
		map.addAttribute("list", service.SelectedList(loginId));
		return "docx/slist";
	}

	// 내가 작성한 문서만 출력
	@GetMapping("/mylist")
	public String myList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size,
			ModelMap map, @RequestParam String writer) {
		List<DocxDto> docxList = service.getUserDocxByPagination(writer, page, size);
		int totalCount = service.getUserDocxCount(writer);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		map.addAttribute("mylist", docxList);
		map.addAttribute("currentPage", page);
		map.addAttribute("totalPages", totalPages);
		map.addAttribute("pageSize", size);
		return "docx/mylist";
	}
	
//	 // 승인된 문서 목록 페이징 처리
//    @GetMapping("/approved")
//    public String getApprovedDocx(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, ModelMap map) {
//        List<DocxDto> approvedList = service.getApprovedDocx(page, size);
//        int totalCount = service.getApprovedDocxCount();
//        int totalPages = (int) Math.ceil((double) totalCount / size);
//        map.addAttribute("approvedList", approvedList);
//        map.addAttribute("approvedCurrentPage", page);
//        map.addAttribute("approvedTotalPages", totalPages);
//        map.addAttribute("approvedPageSize", size);
//        return "docx/approved";
//    }
//    
//    // 미승인된 문서 목록 페이징 처리
//    @GetMapping("/unapproved")
//    public String getUnapprovedDocx(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, ModelMap map) {
//        List<DocxDto> unapprovedList = service.getUnapprovedDocx(page, size);
//        int totalCount = service.getUnapprovedDocxCount();
//        int totalPages = (int) Math.ceil((double) totalCount / size);
//        map.addAttribute("unapprovedList", unapprovedList);
//        map.addAttribute("currentPage", page);
//        map.addAttribute("totalPages", totalPages);
//        map.addAttribute("pageSize", size);
//        return "docx/unapproved";
//    }

	// 검색 컨트롤러
	@PostMapping("/list")
	public String list(Model model,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "15") int size) {
		List<DocxDto> resultList;
		if ("title".equals(searchType)) {
            resultList = service.getByTitleWithPagination(searchValue, page, size);
        } else if ("writer".equals(searchType)) {
            resultList = service.getByWriterWithPagination(searchValue, page, size);
        } else {
            resultList = List.of(); // 기본 빈 리스트를 반환
        }
		int totalItems = resultList.size(); // 전체 아이템 수
        int totalPages = (int) Math.ceil((double) totalItems / size); // 전체 페이지 수
		model.addAttribute("list", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
		return "docx/list";
	}
	
	//보고서 상세페이지 출력
	@GetMapping("/getdocx")
	public String get(int formnum, int docxkey, String formtype, ModelMap map, HttpSession session) {
		boolean flag = false;
		String loginid = (String) session.getAttribute("loginId");
		List<DocxDto> l = service.findByDocxKeyTypeSenior(docxkey, formtype);
		for (DocxDto d : l) {
			if (d.getOrderloc() == d.getDocxorder() && d.getSenior().equals(loginid)) {
				flag = true;
				break;
			}
		}
		map.addAttribute("d", service.getDocx(formnum));
		map.addAttribute("docx", l);
		map.addAttribute("flag", flag);
		return "docx/detail";
	}
	
	
	
	//휴가 신청서 상세페이지
	@GetMapping("/getvacation")
	public String getVacation(int formnum, int docxkey, String formtype, ModelMap map, HttpSession session) {
		boolean flag = false;
		String loginid = (String) session.getAttribute("loginId");

		List<DocxDto> l = service.findByDocxKeyTypeSenior(docxkey, formtype);
		for (DocxDto d : l) {
			if (d.getOrderloc() == d.getDocxorder() && d.getSenior().equals(loginid)) {
				flag = true;
				break;
			}
		}
		map.addAttribute("d", service.getDocx(formnum));
		map.addAttribute("docx", l);
		map.addAttribute("flag", flag);
		System.out.println("현재 문서의 정보 출력 : " + service.findByDocxKeyTypeSenior(docxkey, formtype));
		return "docx/vacationdetail";
	}

	// 결재처리 메서드

	@PostMapping("/approve")
	public String approveDocx(int docxkey, String formtype) {
		service.approveDocx(docxkey, formtype);
		return "redirect:/auth/docx/list";
	}

	@RequestMapping("/deldocx")
	public String deldocx(int docxkey) {
		service.delDocx(docxkey);
		return "redirect:/auth/docx/list";
	}
	
	// 전체문서 리스트 출력
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@GetMapping("/mainList")
	public Map mainList() {
		List<DocxDto> docxList = service.getAllByDocxorderWithPagination(1, 5);
		int totalCount = service.getTotalCountByDocxorder();
		int totalPage = (int) Math.ceil((double) totalCount / 5);
	    Map map = new HashMap<>();
	    map.put("list", docxList);
		return map;
	}

}
