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

	// 페이지로 전체출력 n.getFormtype().equals("dept")
	public List<NoticeDto> getAllNoticePage(int page, int size, String formtype) {
		PageRequest pageRequest = PageRequest.of(page, size);
		List<NoticeDto> filterAllpage = new ArrayList<>();
		Page<Notice> npage = ndao.findByFormtype(formtype, pageRequest);
		for(Notice n : npage) {
			if(n.getFormtype().equals(formtype)){
				filterAllpage.add(new NoticeDto(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(), n.getContent(), n.getFormtype(), n.getWritername()));
			}
		}
		return filterAllpage;
	}
	
	//부서별
	public List<NoticeDto> getDeptNoticePage(int page, int size, String dept) {
		PageRequest pageRequest = PageRequest.of(page, size);
		List<NoticeDto> filterDeptpage = new ArrayList<>();
		Page<Notice> npage = ndao.findByFormtype(dept, pageRequest);
		for(Notice n : npage) {
			if(n.getFormtype().equals(dept)){
				filterDeptpage.add(new NoticeDto(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(), n.getContent(), n.getFormtype(), n.getWritername()));
			}
		}
		return filterDeptpage;
	}
	
	//이름 검색
	public List<NoticeDto> getNoticePageTitle(int page, int size, String title) {
		PageRequest pageRequest = PageRequest.of(page, size);
		List<NoticeDto> filtertitlepage = new ArrayList<>();
		Page<Notice> npage = ndao.findByTitleContaining(title, pageRequest);
		for(Notice n : npage) {
				filtertitlepage.add(new NoticeDto(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(), n.getContent(), n.getFormtype(), n.getWritername()));
		}
		return filtertitlepage;
	}

	public List<NoticeDto> getNoticePageWriter(int page, int size, String writer) {
		PageRequest pageRequest = PageRequest.of(page, size);
		List<NoticeDto> filterwriterpage = new ArrayList<>();
		Page<Notice> npage = ndao.findByWriter_UsernmContaining(writer, pageRequest);
		for(Notice n : npage) {
			filterwriterpage.add(new NoticeDto(n.getId(), n.getWriter(), n.getStartdt(), n.getEnddt(), n.getTitle(), n.getContent(), n.getFormtype(), n.getWritername()));
	}
	return filterwriterpage;
	}

	public void deleteNotice(Long noticeId) {
		ndao.deleteById(noticeId);
	}
	
	public Optional<Notice> getNoticeDetail(Long id){
		return ndao.findById(id);
	}
}
