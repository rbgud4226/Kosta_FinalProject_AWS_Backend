package com.example.demo.oracledb.chat.Room;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatRoomController {
	@Autowired
	private ChatRoomService chatRoomService;
	
	@GetMapping("/chat/chatroom")
	public String createChatRoom(@RequestParam(name = "userid") List<String> userid, HttpSession session, ModelMap map) {
		ModelMap chatRoomInfo = chatRoomService.createChatRoomByUserList(userid, session);
		map.addAttribute("partId", chatRoomInfo.get("partId"));
		map.addAttribute("roomId", chatRoomInfo.get("roomId"));
		map.addAttribute("userId1", chatRoomInfo.get("userId1"));
		return "chat/bootchat";
	}

	@GetMapping("/chat/chatrooms/{userid}")
	public String getChatRoomsByUserId(@PathVariable String userid, HttpSession session, ModelMap map) {
		ModelMap chatRoomInfo = chatRoomService.chatroomsByUserId(userid, session);
		map.addAttribute("partId", chatRoomInfo.get("partId"));
		map.addAttribute("chatRooms", chatRoomInfo.get("chatRooms"));
		map.addAttribute("userId1", chatRoomInfo.get("userId1"));
		return "chat/bootchat";
	}
	

	@GetMapping("/chat/chatrooms/loadrooms/{userid}")
	@ResponseBody
	public ArrayList<ChatRoomDto> getChatRoomsForRecentAndLoad(@PathVariable String userid) {
		return chatRoomService.recentAndLoad(userid);
	}

	@GetMapping("/chat/chatrooms/loadrooms/search/{userid}")
	@ResponseBody
	public ArrayList<ChatRoomDto> getChatRoomsSearch(@PathVariable String userid, HttpSession session) {
		return chatRoomService.chatRoomsSearch(userid, session);		
	}

	@GetMapping("/chat/chatrooms/loadrooms/searchroom/{chatroomid}/{userId1}")
	@ResponseBody
	public ChatRoomDto getChatRoomsConnect(@PathVariable String chatroomid, @PathVariable String userId1) {
		return chatRoomService.chatRoomsConnect(chatroomid, userId1);
	}
	
	@PostMapping("/chat/chatrooms/out")
	@ResponseBody
	public void getOutRoom(@RequestParam("roomId") String roomId, @RequestParam("userId") String userId) {
		chatRoomService.getoutChatRoomMethod(roomId, userId);
	}
		
	@PostMapping("/chat/chatrooms/invite")
	@ResponseBody
	public void inviteChatRoom(@RequestParam List<String> userid, @RequestParam String chatroomid, HttpSession session) {
	    String loginId = (String) session.getAttribute("loginId");
	    chatRoomService.inviteChatRoomMethod(userid, chatroomid, loginId);
	}
	
	@PostMapping("/chat/chatrooms/edit")
	@ResponseBody
	public void editRoomName(@RequestParam String chatroomid, String newRoomName, String userId1) {
		chatRoomService.editChatRoomName(chatroomid, newRoomName, userId1);
	}
}
