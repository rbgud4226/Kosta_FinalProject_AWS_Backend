package com.example.demo.oracledb.chat.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public Map createChatRoom(List<String> userid) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		ModelMap chatRoomInfo = chatRoomService.createChatRoomByUserList(userid, loginId);
		return chatRoomInfo;
	}
	
	//유저 이름으로 채팅방 불러옴
	@PostMapping("/chat/chatrooms")
	public Map getChatRoomsByUserId(String userid) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		ModelMap chatRoomInfo = chatRoomService.chatroomsByUserId(userid, loginId);
		return chatRoomInfo;
	}

	// 채팅방 불러오기(유저 아이디로)
	@PostMapping("/chat/chatrooms/loadrooms")
	public Map getChatRoomsForRecentAndLoad(String userid) {
		Map map = new HashMap();
		map.put("list", chatRoomService.recentAndLoad(userid));
		return map;
	}

	// 채팅방 검색(유저 이름으로)
	@PostMapping("/chat/chatrooms/loadrooms/search")
	public Map getChatRoomsSearch(String userName) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		map.put("list", chatRoomService.chatRoomsSearch(userName, loginId));
		return map;
	}

	// 채팅방 연결하기(채팅방 번호, userId), centerStyle
	@PostMapping("/chat/chatrooms/loadrooms/searchroom")
	public Map getChatRoomsConnect(String chatroomid, String userid) {
		Map map = new HashMap();
		map.put("chatroom", chatRoomService.chatRoomsConnect(chatroomid, userid));
		return map;
	}

	// 채팅방 나가기(채팅방 번호, 나가는사람 id)
	@PostMapping("/chat/chatrooms/out")
	public Map getOutRoom(String chatroomid, String userid) {
		Map map = new HashMap();
		boolean flag = false;
		try {
			chatRoomService.getoutChatRoomMethod(chatroomid, userid);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 채팅방 초대하기(채팅방 번호, 초대할 유저 id 리스트, 로그인한 id)
	@PostMapping("/chat/chatrooms/invite")
	public Map inviteChatRoom(List<String> userids, String chatroomid) {
		System.out.println("요청은 오냐?");
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		boolean flag = false;
		try {
			chatRoomService.inviteChatRoomMethod(userids, chatroomid, loginId);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	//채팅방 이름 수정, 채팅방번호, 새로운이름, 로그인id
	@PutMapping("/chat/chatrooms/edit")
	public Map editRoomName(String chatroomid, String newRoomName) {
		Map map = new HashMap();
		boolean flag = false;
		try {
			String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
			chatRoomService.editChatRoomName(chatroomid, newRoomName, loginId);
			flag = true;
		} catch (IOException e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
