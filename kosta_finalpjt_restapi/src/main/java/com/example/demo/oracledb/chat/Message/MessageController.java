package com.example.demo.oracledb.chat.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;

	@Value("${spring.servlet.multipart.location}")
	private String path;
	
	@MessageMapping("/chat/message/{roomId}")
	public void sendMessage(@Payload MessageDto chatMessage, @DestinationVariable String roomId) {
		if(chatMessage.getType().equals("OUT")) {
			messageService.save(chatMessage, roomId);
			ArrayList<MessageDto> list = messageService.getMessageByRoomId(roomId);
	        messagingTemplate.convertAndSend("/room/" + roomId, list);
	        messagingTemplate.convertAndSend("/recent/update", roomId);
		} else if (chatMessage.getType().equals("FILE")) {
			messageService.fileTypeMessage(chatMessage, roomId);
	        messageService.save(chatMessage, roomId);
	        ArrayList<MessageDto> list = messageService.getMessageByRoomId(roomId);
	        messagingTemplate.convertAndSend("/room/" + roomId, list);
	        messagingTemplate.convertAndSend("/recent/update", roomId);
	    } else if (chatMessage.getType().equals("INVITE")) {
	        messageService.save(chatMessage, roomId);
	        ArrayList<MessageDto> list = messageService.getMessageByRoomId(roomId);
	        messagingTemplate.convertAndSend("/room/" + roomId, list);
	        messagingTemplate.convertAndSend("/recent/update", roomId);
	    } else {
	        messageService.save(chatMessage, roomId);
	        ArrayList<MessageDto> list = messageService.getMessageByRoomId(roomId);
	        messagingTemplate.convertAndSend("/room/" + roomId, list);
	        messagingTemplate.convertAndSend("/recent/update", roomId);
	    }
	}
	
	//채팅방 메세지 호출
	@GetMapping("/chat/message/room/{roomid}")
	@ResponseBody
	public Map getMessages(@PathVariable String roomid) {
		Map map = new HashMap();
		ArrayList<MessageDto> list = messageService.getMessageByRoomId(roomid);
		map.put("list", list);
		return map;
	}
	
	//파일 업로드
	@PostMapping("/chat/message/upload")
	@ResponseBody
	public Map FileUpload(@RequestParam("file") MultipartFile file) {
		return messageService.FileuploadMethod(file);
	}
}
