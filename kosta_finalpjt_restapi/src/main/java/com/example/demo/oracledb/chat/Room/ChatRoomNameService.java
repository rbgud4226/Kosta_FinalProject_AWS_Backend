package com.example.demo.oracledb.chat.Room;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomNameService {
	@Autowired
	private ChatRoomNameDao chatRoomNameDao;

    public ChatRoomName save(ChatRoomNameDto chatRoomNameDto) {
        ChatRoomName chatRoomName = ChangeChatNameDto(chatRoomNameDto);
        return chatRoomNameDao.save(chatRoomName);
    }

    private ChatRoomName ChangeChatNameDto(ChatRoomNameDto chatRoomNameDto) {
        ChatRoomName chatRoomName = new ChatRoomName();
        chatRoomName.setId(chatRoomNameDto.getId());
        chatRoomName.setRoom(chatRoomNameDto.getRoom()); 
        chatRoomName.setHost(chatRoomNameDto.getHost());
        chatRoomName.setRoomName(chatRoomNameDto.getRoomName());
        chatRoomName.setEditableName(chatRoomNameDto.getEditableName());
        return chatRoomName;
    }
	
	public ArrayList<ChatRoomNameDto> getChatRoomNames(String chatroomid){
		List<ChatRoomName> l = chatRoomNameDao.findByRoom_chatroomid(chatroomid);
		ArrayList<ChatRoomNameDto> list = new ArrayList<>();
		for(ChatRoomName crn : l) {
			list.add(new ChatRoomNameDto(crn.getId(), crn.getRoom(),crn.getHost(), crn.getRoomName(), crn.getEditableName()));
		}
		return list;
	} 
}
