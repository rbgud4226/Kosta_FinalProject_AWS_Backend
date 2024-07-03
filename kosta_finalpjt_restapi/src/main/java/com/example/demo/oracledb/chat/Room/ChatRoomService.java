package com.example.demo.oracledb.chat.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.example.demo.oracledb.chat.Message.MessageController;
import com.example.demo.oracledb.chat.Message.MessageDto;
import com.example.demo.oracledb.chat.Message.MessageService;
import com.example.demo.oracledb.chat.RoomUser.RoomUserService;
import com.example.demo.oracledb.members.MembersService;
import com.example.demo.oracledb.users.Users;
import com.example.demo.oracledb.users.UsersDao;
import com.example.demo.oracledb.users.UsersService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class ChatRoomService {

	@Autowired
	private ChatRoomDao chatRoomDao;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private UsersService usersService;

	@Autowired
	private RoomUserService roomUserService;

	@Autowired
	private ChatRoomNameDao chatRoomNameDao;
	
	@Autowired
	private ChatRoomNameService chatRoomNameService;

	@Autowired
	private MembersService memberService;
	
	@Autowired
	@Lazy
	private MessageService messageService;

	@Autowired
	private MessageController messageController;
	
	public ChatRoomDto createChatRoom(List<String> userIds) {
		String name = createChatRoomName(userIds);
		ChatRoom chatRoom = chatRoomDao.findByName(name);
		if (chatRoom != null) {
			if (!chatRoom.isStatus()) {
				chatRoom.setStatus(true);
				chatRoomDao.save(chatRoom);
			}
			if (!chatRoom.getRoomType().equals("PERSONAL") && !chatRoom.getRoomType().equals("PRIVATE")) {
				chatRoom = createNewChatRoom(userIds, name);
			}
			return new ChatRoomDto(chatRoom.getChatroomid(), chatRoom.getName(), chatRoom.getChatRoomNames(),
					chatRoom.getRoomType(), chatRoom.getChats(), chatRoom.getRoomUsers(), chatRoom.isStatus(), null,
					null, null);
		} else {
			chatRoom = createNewChatRoom(userIds, name);
		}
		return new ChatRoomDto(chatRoom.getChatroomid(), chatRoom.getName(), chatRoom.getChatRoomNames(),
				chatRoom.getRoomType(), chatRoom.getChats(), chatRoom.getRoomUsers(), chatRoom.isStatus(), null, null,
				null);
	}

	private ChatRoom createNewChatRoom(List<String> userIds, String name) {
		List<String> participantsN = new ArrayList<>();
		for (String s : userIds) {
			participantsN.add(usersService.getById2(s).getUsernm());
		}
		String partN = createPartName(participantsN);

		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setChatroomid(UUID.randomUUID().toString());
		chatRoom.setName(name);
		chatRoom.setParticipants(partN);
		String[] l = chatRoom.getName().split("_");
		if (l.length > 2) {
			chatRoom.setRoomType("GROUP");
		} else if (l.length == 1) {
			chatRoom.setRoomType("PRIVATE");
		} else {
			chatRoom.setRoomType("PERSONAL");
		}
		chatRoom.setStatus(true);
		chatRoomDao.save(chatRoom);

		for (String roomName : l) {
			ChatRoomName chatRoomName = new ChatRoomName();
			chatRoomName.setRoom(chatRoom);
			chatRoomName.setHost(roomName);
			chatRoomName.setRoomName(name);
			chatRoomName.setEditableName(partN);
			chatRoomNameDao.save(chatRoomName);
		}

		for (String userid : userIds) {
			Users user = usersService.getById2(userid);
			roomUserService.save(user, chatRoom);
		}
		return chatRoom;
	}

	public String getOutChatRoom(String chatroomid, String userid) {
		String getOutMessage = "";
		ChatRoom chatRoom = chatRoomDao.findByChatroomid(chatroomid);
		if (chatRoom.getRoomType().equals("GROUP") || chatRoom.getRoomType().equals("PERSONAL")) {
			String[] userIds = chatRoom.getName().split("_");
			String[] partis = chatRoom.getParticipants().split("_");
			String part = usersService.getById2(userid).getUsernm();
			List<String> userIdList = new ArrayList<>(Arrays.asList(userIds));
			List<String> partisList = new ArrayList<>(Arrays.asList(partis));

			partisList.remove(part);
			userIdList.remove(userid);

			if (partisList.isEmpty()) {
				chatRoom.setStatus(false);
			} else {
				String addUserIds = createChatRoomName(userIdList);
				chatRoom.setName(addUserIds);
				chatRoom.setParticipants(createPartName(partisList));
				getOutMessage = part + "님이 나갔습니다";
			}
			chatRoomDao.save(chatRoom);
		}else {
			getOutMessage = "PRIVATE 방은 나갈수 없습니다";
		}
		return getOutMessage;
	}

	public ArrayList<ChatRoomDto> getChatRoomsListByName(String name, String loginId) {
		List<ChatRoom> l = chatRoomDao.findAll();
		String partN = usersService.getById2(loginId).getUsernm();
		ArrayList<ChatRoomDto> list = new ArrayList<>();
		for (ChatRoom cr : l) {
			if (cr.getParticipants().contains(partN) && cr.getParticipants().contains(name) && cr.isStatus()) {
				list.add(new ChatRoomDto(cr.getChatroomid(), cr.getName(), cr.getChatRoomNames(), cr.getRoomType(),
						cr.getChats(), cr.getRoomUsers(), cr.isStatus(), null, cr.getParticipants(), null));
			}
		}
		return list;
	}

	public ArrayList<ChatRoomDto> getAllChatRooms(String loginId) {
		List<ChatRoom> l = chatRoomDao.findAll();
		ArrayList<ChatRoomDto> list = new ArrayList<>();
		for (ChatRoom cr : l) {
			String imgL = "";
			if (cr.getRoomType().equals("PERSONAL") || cr.getRoomType().equals("PRIVATE")) {
				ArrayList<String> nameList = new ArrayList<>();
				for (String name : cr.getName().split("_")) {
					if (!name.equals(loginId)) {
						nameList.add(name);
					}
				}
				for (String name : nameList) {
					if (name.equals(loginId)) {
						String userId = usersService.getById2(loginId).getId();
						imgL = memberService.getByuserId(userId).getMemberimgnm();
						break;
					} else {
						String userId = usersService.getById2(name).getId();
						imgL = memberService.getByuserId(userId).getMemberimgnm();
					}
				}
			} else {
				imgL = "";
			}
			if (cr.isStatus() && cr.getName().contains(loginId)) {
				list.add(new ChatRoomDto(cr.getChatroomid(), cr.getName(), cr.getChatRoomNames(), cr.getRoomType(),
						cr.getChats(), cr.getRoomUsers(), cr.isStatus(), null, cr.getParticipants(), imgL));
			}
		}
		return list;
	}

	public ChatRoomDto getChatRoomsByChatRoomId(String chatroomid, String userId1) {
		ChatRoom c = chatRoomDao.findByChatroomid(chatroomid);
		String imgL = "";
		if (c.getRoomType().equals("PERSONAL") || c.getRoomType().equals("PRIVATE")) {
			ArrayList<String> nameL = new ArrayList<>();
			for (String l : c.getName().split("_")) {
				nameL.add(l);
			}
			for (String l : nameL) {
				if (l.equals(userId1)) {
					String userid = usersService.getById2(userId1).getId();
					imgL = memberService.getByuserId(userid).getMemberimgnm();
				}
				String userid = usersService.getById2(l).getId();
				imgL = memberService.getByuserId(userid).getMemberimgnm();
			}
			ChatRoomDto cr = new ChatRoomDto(c.getChatroomid(), c.getName(), c.getChatRoomNames(), c.getRoomType(),
					c.getChats(), c.getRoomUsers(), c.isStatus(), null, c.getParticipants(), imgL);
			return cr;
		} else {
			imgL = "";
			ChatRoomDto cr = new ChatRoomDto(c.getChatroomid(), c.getName(), c.getChatRoomNames(), c.getRoomType(),
					c.getChats(), c.getRoomUsers(), c.isStatus(), null, c.getParticipants(), imgL);
			return cr;
		}
	}
	
	@Transactional
	public void editChatRoomName(String chatroomid, String newRoomName, String userId1) {
		ChatRoom chatRoom = chatRoomDao.findByChatroomid(chatroomid);
		if (chatRoom == null) {
			throw new NullPointerException("채팅방이 존재하지 않습니다.");
		}
		List<ChatRoomName> roomNames = chatRoom.getChatRoomNames();

		for (ChatRoomName crn : roomNames) {
			if (crn.getHost().equals(userId1)) {
				if (newRoomName == null || newRoomName.trim().isEmpty()) {
					crn.setEditableName("채팅방 이름 없음");
				} else {
					crn.setEditableName(newRoomName);
				}
			}
		}
		chatRoomDao.save(chatRoom);
	}

	public ArrayList<String> inviteUserToChatRoom(String chatroomid, List<String> newuserid, String loginId) {
		ArrayList<String> inviteMessage = new ArrayList<>();
		String partN = "";
		ChatRoom chatRoom = chatRoomDao.findByChatroomid(chatroomid);
		if (chatRoom == null) {
			throw new NullPointerException("채팅방이 존재하지 않습니다.");
		}
		if (chatRoom.getRoomType().equals("PRIVATE")) {
			inviteMessage.add("PRIVATE 방은 사용자를 초대할수 없습니다");
			return inviteMessage;
		}
		for (String s : newuserid) {
			if (usersDao.findById(s) != null) {
				String[] userIds = chatRoom.getName().split("_");
				String[] partis = chatRoom.getParticipants().split("_");
				partN = usersService.getById2(s).getUsernm(); // 이름
				ArrayList<String> userIdList = new ArrayList<>(Arrays.asList(userIds));
				List<String> partisList = new ArrayList<>(Arrays.asList(partis));
				if (chatRoom.getRoomType().equals("PERSONAL") && userIds.length == 1) {
				        inviteMessage.add("PERSONAL 방은 사용자 한 명만 있을 때 초대할 수 없습니다");
				        return inviteMessage;
				    }
				 
				if (userIdList.contains(s)) {
					inviteMessage.add(partN + "은 이미 방에 있습니다");
				} else if (!userIdList.contains(s)) {
					userIdList.add(s);	
					partisList.add(partN);
					chatRoom.setRoomType("GROUP");
					inviteMessage.add(partN + "님이 초대완료 되었습니다");
					
					ChatRoomNameDto newChatRoomNameDto = new ChatRoomNameDto();
	                newChatRoomNameDto.setRoom(chatRoom);
	                newChatRoomNameDto.setHost(s);
	                newChatRoomNameDto.setEditableName(partN);
	                newChatRoomNameDto.setRoomName(createChatRoomName(userIdList));
	                chatRoomNameService.save(newChatRoomNameDto);
				}
				String addUserIds = createChatRoomName(userIdList);
				String partNs = createPartName(partisList);
				chatRoom.setName(addUserIds);
				chatRoom.setParticipants(partNs);
				ArrayList<ChatRoomNameDto> chatroomN = chatRoomNameService.getChatRoomNames(chatroomid);
				for(ChatRoomNameDto crnd : chatroomN) {
					crnd.setEditableName(partNs);
					crnd.setRoomName(addUserIds);
					chatRoomNameService.save(crnd);
				}
				chatRoomDao.save(chatRoom);
			} else {
				inviteMessage.add(partN + "는 사용자목록에 없는 사용자 입니다");
				return inviteMessage;
			}
		}
		return inviteMessage;
	}
	
	public MessageDto createInviteMessage(List<String> userid, String chatroomid, String loginId, String inviteContent) {
	    MessageDto inviteMessage = new MessageDto();
	    inviteMessage.setType("INVITE");
	    inviteMessage.setContent(inviteContent);
	    inviteMessage.setPartid(usersService.getById2(loginId).getUsernm());
	    inviteMessage.setSender(loginId);
	    inviteMessage.setSendDate(createSendDate());
	    return inviteMessage;
	}
	
	public MessageDto getOutMessage(String roomId, String userId, String outContent) {
	    MessageDto getOutMessage = new MessageDto();
	    getOutMessage.setType("OUT");
	    getOutMessage.setContent(outContent);
	    getOutMessage.setPartid(usersService.getById2(userId).getUsernm());
	    getOutMessage.setSender(userId);
	    getOutMessage.setSendDate(createSendDate());
	    return getOutMessage;
	}

	public String createChatRoomName(List<String> userIds) {
		Collections.sort(userIds);
		return String.join("_", userIds);
	}
	
	public String createPartName(List<String> partName) {
		return String.join("_", partName);
	}
	
	public String createSendDate() {
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MMMM dd일", Locale.KOREAN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.KOREAN);
        String sendDay = now.format(dateFormatter);
        String sendTime = now.format(timeFormatter);
        return sendDay + " " + sendTime;
	}
	
	public List<ChatRoom> getChatRoomByStatusF(){
		return chatRoomDao.findByStatus(false);
	}
	
	
	public void delChatRoomBychatroomid() {
		chatRoomDao.deleteByStatus(false);
	}
	
	//controller createChatRoom
	public ModelMap createChatRoomByUserList(List<String> userid, HttpSession session) {
		ModelMap   map = new ModelMap();
		String userId1 = (String) session.getAttribute("loginId");
		if (!userid.contains(userId1)) {
			userid.add(userId1);
		}
		ChatRoomDto chatRoomDto = createChatRoom(userid);
		String partId = usersService.getById2(userId1).getUsernm();
		map.addAttribute("partId", partId);
		map.addAttribute("roomId", chatRoomDto.getChatroomid());
		map.addAttribute("userId1", userId1);
		return map;
	}
	
	//controller getChatRoomsForRecentAndLoad
	public ArrayList<ChatRoomDto> recentAndLoad(String userid) {
		ArrayList<ChatRoomDto> cr = getAllChatRooms(userid);
		for (ChatRoomDto chatRoom : cr) {
			String recentMsg = messageService.getRecentMessageByRoomId(chatRoom.getChatroomid());
			ArrayList<ChatRoomNameDto> roomNamesDto = chatRoomNameService.getChatRoomNames(chatRoom.getChatroomid());
			List<ChatRoomName> roomNames = new ArrayList<>();
			for (ChatRoomNameDto dto : roomNamesDto) {
				if (dto.getHost().equals(userid)) {
					roomNames.add(new ChatRoomName(dto.getId(), dto.getRoom(),dto.getHost(), dto.getRoomName(), dto.getEditableName()));   
	            }
			}
			chatRoom.setRecentMsg(recentMsg);
			chatRoom.setChatRoomNames(roomNames);
		}
		return cr;
	}
	
	//controller getChatRoomsSearch
	public ArrayList<ChatRoomDto> chatRoomsSearch(String userid, HttpSession session){
		String loginId = (String) session.getAttribute("loginId");
		ArrayList<ChatRoomDto> cr = getChatRoomsListByName(userid, loginId);
		for (ChatRoomDto chatRoom : cr) {
			String recentMsg = messageService.getRecentMessageByRoomId(chatRoom.getChatroomid());
			ArrayList<ChatRoomNameDto> roomNamesDto = chatRoomNameService.getChatRoomNames(chatRoom.getChatroomid());
			List<ChatRoomName> roomNames = new ArrayList<>();
			for (ChatRoomNameDto dto : roomNamesDto) {
				if (dto.getHost().equals(loginId)) {
					roomNames.add(new ChatRoomName(dto.getId(), dto.getRoom(),dto.getHost(), dto.getRoomName(), dto.getEditableName()));   
	            }
			}
			chatRoom.setRecentMsg(recentMsg);
			chatRoom.setChatRoomNames(roomNames);
		}
		return cr;
	}
	
	//controller getChatRoomsConnect
	public ChatRoomDto chatRoomsConnect(String chatroomid, String userId1) {
		ChatRoomDto cr = getChatRoomsByChatRoomId(chatroomid, userId1);
	    ArrayList<ChatRoomNameDto> roomNamesDto = chatRoomNameService.getChatRoomNames(cr.getChatroomid());
	    List<ChatRoomName> roomNames = new ArrayList<>();
	    for (ChatRoomNameDto dto : roomNamesDto) {
	        if (dto.getHost().equals(userId1)) {
	            roomNames.add(new ChatRoomName(dto.getId(), dto.getRoom(), dto.getHost(), dto.getRoomName(), dto.getEditableName()));
	        }
	    }
	    cr.setChatRoomNames(roomNames);
	    return cr;
	}
	
	//controller inviteChatRoom
	public void inviteChatRoomMethod(List<String> userid, String chatroomid, String loginId) {
		    ArrayList<String> mes = inviteUserToChatRoom(chatroomid, userid, loginId);
		    String inviteContent = String.join("<br/>", mes);
		    MessageDto inviteMessage = createInviteMessage(userid, chatroomid, loginId, inviteContent);
		    messageController.sendMessage(inviteMessage, chatroomid);
	}
	
	//controller getOutRoom
	public void getoutChatRoomMethod(String roomId, String userId) {
		String mes = getOutChatRoom(roomId, userId);
		MessageDto getOutMessage = getOutMessage(roomId, userId, mes);
		messageController.sendMessage(getOutMessage, roomId);
	}
	
	//controller getChatRoomsByUserId
	public ModelMap  chatroomsByUserId(String userid, HttpSession session) {
		ModelMap map = new ModelMap();
		String userId1 = (String) session.getAttribute("loginId");
		ArrayList<ChatRoomDto> cr = getChatRoomsListByName(userid, userId1);
		String partId = usersService.getById2(userId1).getUsernm();
		map.addAttribute("partId", partId);
		map.addAttribute("chatRooms", cr);
		map.addAttribute("userId1", userId1);
		return map;
	}
	
	//controller getChatRoomByRoomId
	public ModelMap chatRoomByRoomIdMethod(String roomId, HttpSession session){
		ModelMap map = new ModelMap();
		String userId1 = (String) session.getAttribute("loginId");
		String partId = usersService.getById2(userId1).getUsernm();
		map.addAttribute("partId", partId);
		map.addAttribute("roomId", roomId);
		map.addAttribute("userId1", userId1);
		return map;
	}

}
