package com.example.demo.oracledb.docx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.oracledb.auth.MyTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/docx") 
public class DocxController {

	@Autowired
	private DocxService service;
	
	@Autowired
	private MyTokenProvider tokenProvider;	


	// 작성폼 불러올때 멤버 리스트 데이터 가져오기
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/addreport")
	@ResponseBody
	public Map<String, Object> reportForm() {
		Map<String , Object> map = new HashMap();
		map.put("mlist", service.getMemAll());
		map.put("view", "docx/addreport");
		return map;
	}

	// 보고서 작성양식
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/addreport")
	public boolean addreport(DocxDto d) {
		String senior = d.getSenior();
		System.out.println("시니어 저장 형식 확인 ::::" + senior);
		if (senior != null) {
			int dkey = service.findByFormtypeDesc(d.getFormtype());
			dkey += 1;
			String[] s = d.getSenior().split(",");
			
			for (int j = 0; j < s.length; j++) {
				System.out.println("스플릿 된 시니어 값 확인 :::::" + s[j]);
				service.save(d, s[j], null, j, dkey);
			}
		}
//		Map<String , String> map = new HashMap();
//		map.put("redirect","/auth/docx/list");
		return true;
	}

	// 휴가서류 작성양식
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/addvacation")
	public boolean addvacation(DocxDto d) {
		int dkey = service.findByFormtypeDesc(d.getFormtype());
		dkey += 1;
		//문자열로 받아온 senior 를 콤마 기준으로 잘라서 배열에 담음
		String[] s = d.getSenior().split(",");
		//for문으로 배열에 담긴 만큼 돌려 각 문서에 한명씩 저장
		for (int j = 0; j < s.length; j++) {
			service.save(d, s[j], null, j, dkey);
		}
//		Map<String,String> map = new HashMap();
//		map.put("redirect", "/auth/docx/list");
		return true;
	}

	// 회의록 작성양식
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/addmeeting")
	public Map<String , String> addmeeting(DocxDto d) {
		String senior = d.getSenior();
		int dkey = service.findByFormtypeDesc(d.getFormtype());
		dkey += 1;
		if (senior != null) {
			String[] s = d.getSenior().split(",");
			for (int j = 0; j < s.length; j++) {
				service.save(d, null, d.getParticipant(), j, dkey);
			}
		}
		Map<String,String> map = new HashMap();
		map.put("redirect","/auth/docx/list");
		return map;
	}

	// 전체문서 리스트 출력
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/list")
	public Map<String,Object> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size) {
		List<DocxDto> docxList = service.getAllByDocxorderWithPagination(page, size);
		int totalCount = service.getTotalCountByDocxorder();
		int totalPage = (int) Math.ceil((double) totalCount / size);
		Map<String,Object> map = new HashMap();
		map.put("list", docxList);
		map.put("currentPage", page);
		map.put("totalPages", totalPage);
		map.put("pageSize", size);
		return map;
	}


	// 내가 작성한 문서만 출력
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/mylist")
	public Map<String,Object> myList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, HttpServletRequest request) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String id = auth.getName();
		String token = tokenProvider.resolveToken(request);
		String writer = tokenProvider.getUserName(token);
		
		System.out.println("토큰에서 꺼내온 writer 확인 >>>>>>>>>>" + writer);
		List<DocxDto> docxList = service.getUserDocxByPagination(writer, page, size);
		int totalCount = service.getUserDocxCount(writer);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		Map<String,Object> map = new HashMap();
		map.put("mylist", docxList);
		map.put("currentPage", page);
		map.put("totalPages", totalPages);
		map.put("pageSize", size);
		return map;
	}

	// 검색 컨트롤러
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/list")
	public Map<String, Object> list(
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
        Map<String, Object> map = new HashMap();
		map.put("list", resultList);
		map.put("currentPage", page);
		map.put("pageSize", size);
		map.put("totalPages", totalPages);
		map.put("searchType", searchType);
		map.put("searchValue", searchValue);
		return map;
	}
	
	//보고서 상세페이지 출력
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/getdocx")
	public Map<String,Object> get(@RequestParam(name="formnum")int formnum, @RequestParam(name="docxkey")int docxkey, @RequestParam(name="formtype")String formtype) {
		System.out.println("문서 번호 출력 확인 : " +formnum);
		boolean flag = false;
		
//		String token = tokenProvider.resolveToken(request);
//		String loginid = tokenProvider.getUserName(token);
		String loginid= SecurityContextHolder.getContext().getAuthentication().getName();
		List<DocxDto> l = service.findByDocxKeyTypeSenior(docxkey, formtype);
		for (DocxDto d : l) {
			if (d.getOrderloc() == d.getDocxorder() && d.getSenior().equals(loginid)) {
				flag = true;
				break;
			}
		}
		Map<String,Object> map = new HashMap();
		map.put("d", service.getDocx(formnum));
		map.put("docx", l);
		map.put("flag", flag);
		return map;
	}
	
	
	
	//휴가 신청서 상세페이지
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/getvacation")
	public Map<String,Object> getVacation(int formnum, int docxkey, String formtype, HttpServletRequest request) {
		boolean flag = false;
		String token = tokenProvider.resolveToken(request);
		String loginid = tokenProvider.getUserName(token);

		List<DocxDto> l = service.findByDocxKeyTypeSenior(docxkey, formtype);
		for (DocxDto d : l) {
			if (d.getOrderloc() == d.getDocxorder() && d.getSenior().equals(loginid)) {
				flag = true;
				break;
			}
		}
		Map<String,Object> map = new HashMap();
		map.put("d", service.getDocx(formnum));
		map.put("docx", l);
		map.put("flag", flag);
		return map;
	}

	// 결재처리 메서드
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/approve")
	public Map<String,String> approveDocx(@RequestParam int docxkey,@RequestParam String formtype) {
		Map<String,String> map = new HashMap();
		service.approveDocx(docxkey, formtype);
		map.put("redirect","/auth/docx/list");
		return map;
	}
	
	//작성 글 삭제
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/deldocx")
	public Map<String,String> deldocx(int docxkey) {
		service.delDocx(docxkey);
		Map<String,String> map = new HashMap();
		map.put("redirect", "/auth/docx/list");
		return map;
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
	    map.put("totalPage", totalPage);
		return map;
	}
	
//현재 사용안함	 // 결재걸린 문서 리스트 출력
//	@GetMapping("/slist")
//	public String slist(ModelMap map, HttpSession session) {
//		String loginId = (String) session.getAttribute("loginId");
//		map.addAttribute("list", service.SelectedList(loginId));
//		return "docx/slist";
//	}

}
