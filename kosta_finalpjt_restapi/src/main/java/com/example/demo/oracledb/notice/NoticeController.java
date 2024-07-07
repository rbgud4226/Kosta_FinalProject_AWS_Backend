package com.example.demo.oracledb.notice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.jsonwebtoken.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;

	// 공지 저장
	@PostMapping("/notice/add")
	@ResponseBody
	public Map addNotice(@ModelAttribute  NoticeDto dto) {
		boolean flag = false;
		Map map = new HashMap();
		try {
		noticeService.save(dto);
		flag = true;
		} catch(IOException e ) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 공지 전체 출력
	@GetMapping("/notice/list")
	@ResponseBody
	public Map slist() {
		Map map = new HashMap();
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		map.put("list", noticeService.getAllNotice());
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
		}catch(IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
