package com.example.demo.oracledb.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.oracledb.members.MembersService;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.constraints.Positive;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private MembersService membersService;

	// 공지 저장
	@PostMapping("/notice/add")
	@ResponseBody
	public Map addNotice(@ModelAttribute NoticeDto dto, @RequestParam String dept) {
		boolean flag = false;
		Map map = new HashMap();
		try {
			noticeService.save(dto, dept);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	@GetMapping("/notice/allpagelist")
	@ResponseBody
	public Map pagelist(@Positive @RequestParam int page, @Positive @RequestParam int size, @RequestParam String formtype) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		List<NoticeDto> noticepage = noticeService.getAllNoticePage(page - 1, size, formtype);
		map.put("list", noticepage);
		return map;
	}

	@GetMapping("/notice/deptpagelist")
	@ResponseBody
	public Map deptpagelist(@Positive @RequestParam int page, @Positive @RequestParam int size) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		String dept = membersService.getByuserId(loginId).getDeptid().getDeptnm();
		Map map = new HashMap();
		List<NoticeDto> noticepage = noticeService.getDeptNoticePage(page - 1, size, dept);
		map.put("list", noticepage);
		return map;
	}

	@PostMapping("/notice/titlelist")
	@ResponseBody
	public Map titlelist(@RequestParam String title, @Positive @RequestParam int page,
			@Positive @RequestParam int size) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		String dept = membersService.getByuserId(loginId).getDeptid().getDeptnm();
		List<Notice> fileterList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		List<NoticeDto> noticepage = new ArrayList<>();
		if (title == null || title.isEmpty()) {
		} else {
			noticepage = noticeService.getNoticePageTitle(page - 1, size, title);
		}

		for (NoticeDto n : noticepage) {
			if (n.getFormtype().equals(dept) || n.getFormtype().equals("전체")) {
				fileterList.add(new Notice(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(),
						n.getContent(), n.getFormtype(), n.getWritername()));
			}
		}
		map.put("tlist", fileterList);
		return map;
	}

	@PostMapping("/notice/writerlist")
	@ResponseBody
	public Map writerlist(@RequestParam String writer, @Positive @RequestParam int page,
			@Positive @RequestParam int size) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		String dept = membersService.getByuserId(loginId).getDeptid().getDeptnm();
		List<Notice> filterList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		List<NoticeDto> noticepage = new ArrayList<>();
		if (writer == null || writer.isEmpty()) {
		} else {
			noticepage = noticeService.getNoticePageWriter(page - 1, size, writer);
		}
		for (NoticeDto n : noticepage) {
			if (n.getFormtype().equals(dept) || n.getFormtype().equals("전체")) {
				filterList.add(new Notice(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(),
						n.getContent(), n.getFormtype(), n.getWritername()));
			}
		}
		map.put("wlist", filterList);
		return map;
	}

	@DeleteMapping("/notice/delete")
	@ResponseBody
	public Map deleteNotice(@RequestParam("noticeId") Long noticeId) {
		Map map = new HashMap();
		boolean flag = false;
		try {
			noticeService.deleteNotice(noticeId);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	@PostMapping("/notice/detail")
	@ResponseBody
	public Map noticeDetail(@RequestParam Long id) {
		Map map = new HashMap();
		Notice n = noticeService.getNoticeDetail(id).get();
		map.put("notice", n);
		return map;
	}
}
