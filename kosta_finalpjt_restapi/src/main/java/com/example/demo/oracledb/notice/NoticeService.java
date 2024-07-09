package com.example.demo.oracledb.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.oracledb.users.UsersService;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao ndao;

	@Autowired
	private UsersService usersService;

	// 공지 작성, 수정
	public Notice save(NoticeDto dto, String dept) {
		String writername = usersService.getById(dto.getWriter().getId()).getUsernm();
		Notice notice = ndao.save(new Notice(dto.getId(), dto.getWriter(), dto.getStartdt(), dto.getEnddt(),
				dto.getTitle(), dto.getContent(), dept, writername));
		return notice;
	}

	// 전체 공지 리스트 출력
	public ArrayList<NoticeDto> getAllNotice() {
		List<Notice> l = ndao.findAll();
		ArrayList<NoticeDto> list = new ArrayList<>();
		for (Notice n : l) {
			list.add(new NoticeDto(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(), n.getContent(),
					n.getFormtype(), n.getWritername()));
		}
		return list;
	}

	// 페이지로 전체출력
	public Page<Notice> getAllNoticePage(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return ndao.findAll(pageRequest);
	}

	public Page<Notice> getNoticePageTitle(int page, int size, String title) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return ndao.findByTitleContaining(title, pageRequest);
	}

	public Page<Notice> getNoticePageWriter(int page, int size, String writer) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return ndao.findByWriter_UsernmContaining(writer, pageRequest);
	}

	public void deleteNotice(Long noticeId) {
		ndao.deleteById(noticeId);
	}
	
	public Optional<Notice> getNoticeDetail(Long id){
		return ndao.findById(id);
	}
}
