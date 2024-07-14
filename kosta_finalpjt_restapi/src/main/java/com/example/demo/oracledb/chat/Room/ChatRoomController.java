package com.example.demo.oracledb.chat.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class ChatRoomController {
	@Autowired
	private ChatRoomService chatRoomService;
	
	//채팅방 생성
	@PostMapping("/chat/chatroom")
	@ResponseBody
	public Map createChatRoom(@RequestParam("userid[]") List<String> userid) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		ModelMap chatRoomInfo = chatRoomService.createChatRoomByUserList(userid, loginId);
		return chatRoomInfo;
	}
	
	//유저 이름으로 채팅방 불러옴
	@PostMapping("/chat/chatrooms")
	@ResponseBody
	public Map getChatRoomsByUserId(String userid) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		ModelMap chatRoomInfo = chatRoomService.chatroomsByUserId(userid, loginId);
		return chatRoomInfo;
	}

	// 채팅방 불러오기(유저 아이디로)
	@PostMapping("/chat/chatrooms/loadrooms")
	@ResponseBody
	public Map getChatRoomsForRecentAndLoad(String userid) {
		Map map = new HashMap();
		map.put("list", chatRoomService.recentAndLoad(userid));
		return map;
	}

	// 채팅방 검색(유저 이름으로)
	@PostMapping("/chat/chatrooms/loadrooms/search")
	@ResponseBody
	public Map getChatRoomsSearch(@RequestParam(name="userName") String userName) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		map.put("list", chatRoomService.chatRoomsSearch(userName, loginId));
		return map;
	}

	// 채팅방 연결하기(채팅방 번호, userId), centerStyle ,searhroom 이였음 원래
	@PostMapping("/chat/chatrooms/loadrooms/connect")
	@ResponseBody
	public Map getChatRoomsConnect(@RequestParam String roomid, @RequestParam String userid) {
		Map map = new HashMap();
		map.put("chatroom", chatRoomService.chatRoomsConnect(roomid, userid));
		return map;
	}

	// 채팅방 나가기(채팅방 번호, 나가는사람 id)
	@PostMapping("/chat/chatrooms/out")
	@ResponseBody
	public Map getOutRoom(@RequestParam String roomid, @RequestParam int page) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		boolean flag = false;
		try {
			chatRoomService.getoutChatRoomMethod(roomid, loginId, page);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	//초대 테스트해봐야됨
	@PostMapping("/chat/chatrooms/invite")
	@ResponseBody
	public Map inviteChatRoom(@RequestParam("userid[]") List<String> userid, String chatroomid, @RequestParam int page) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		boolean flag = false;
		try {
			chatRoomService.inviteChatRoomMethod(userid, chatroomid, loginId, page);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	//채팅방 이름 수정, 채팅방번호, 새로운이름, 로그인id
	@PostMapping("/chat/chatrooms/edit")
	@ResponseBody
	public Map editRoomName(@RequestParam(name="roomid") String roomid, @RequestParam(name="newRoomName") String newRoomName) {
		Map map = new HashMap();
		boolean flag = false;
		try {
			String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
			chatRoomService.editChatRoomName(roomid, newRoomName, loginId);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
