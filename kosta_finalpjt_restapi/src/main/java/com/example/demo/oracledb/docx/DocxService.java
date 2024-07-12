package com.example.demo.oracledb.docx;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.oracledb.members.Members;
import com.example.demo.oracledb.members.MembersDao;
import com.example.demo.oracledb.members.MembersDto;
import com.example.demo.oracledb.users.Users;

@Service
public class DocxService {
	@Autowired
	private DocxDao dao;
	
	@Autowired
	private MembersDao mdao;

	// 문서 작성 , 수정
	public DocxDto save(DocxDto dto, String senior, String participant, int i, int dkey) {
		Docx d = dao.save(
				new Docx(dto.getFormnum(), dto.getWriter(), senior, dto.getStartdt(), dto.getEnddt(), dto.getTitle(),
						dto.getContent(), dto.getNote(), dto.getTaskclasf(), dto.getTaskplan(), dto.getTaskprocs(),
						dto.getTaskprocsres(), dto.getDeptandmeetloc(), dto.getDayoffclasf(), dto.getParticipant(),
						dto.getFormtype(), dto.getAprovdoc(), i, dto.getStatus(), dkey, dto.getOrderloc()));

		return new DocxDto(d.getFormnum(), d.getWriter(), d.getSenior(), d.getStartdt(), d.getEnddt(), d.getTitle(),
				d.getContent(), d.getNote(), d.getTaskclasf(), d.getTaskplan(), d.getTaskprocs(), d.getTaskprocsres(),
				d.getDeptandmeetloc(), d.getDayoffclasf(), participant, d.getFormtype(), d.getAprovdoc(),
				d.getDocxorder(), d.getStatus(), d.getDocxkey(), d.getOrderloc());
	}

	// 문서 번호로 검색
	public DocxDto getDocx(int formnum) {
		Docx d = dao.findById(formnum).orElse(null);
		if (d == null) {
			return null;
		}
		return new DocxDto(d.getFormnum(), d.getWriter(), null, d.getStartdt(), d.getEnddt(), d.getTitle(),
				d.getContent(), d.getNote(), d.getTaskclasf(), d.getTaskplan(), d.getTaskprocs(), d.getTaskprocsres(),
				d.getDeptandmeetloc(), d.getDayoffclasf(), d.getParticipant(), d.getFormtype(), d.getAprovdoc(),
				d.getDocxorder(), d.getStatus(), d.getDocxkey(), d.getOrderloc());
	}

	// 결재과정 처리 메서드
	public void approveDocx(int docxkey, String formtype) {
		int test = docxkey;
		System.out.println("docx키 확인 : : " + docxkey);
		System.out.println("formtype 확인 : :-" + formtype);
		System.out.println("docxkey 타입 확인 : :-" + test);
		List<Docx> docxlist = dao.findByDocxkeyAndFormtype(docxkey, formtype);
		System.out.println("문서 가져오는지 확인 : : : " + docxlist);
		int cnt = docxlist.get(0).getOrderloc() + 1;
		for (int i = 0; i < docxlist.size(); i++) {
			Docx d = docxlist.get(i);
			d.setOrderloc(cnt);
			dao.save(d);
			System.out.println("문서 저장 완료됨 ===" + d);
			if (i == docxlist.size() - 1) {
				System.out.println("마지막 문서 처리중 ....");
				if (d.getOrderloc() > d.getDocxorder()) {
					System.out.println("orderloc이 docxorder보다 큼: orderloc = " + d.getOrderloc() + ", docxorder = " + d.getDocxorder());
					for (Docx doc : docxlist) {
						doc.setStatus(2);
						dao.save(doc);
						System.out.println("문서 상태 업데이트됨: " + doc);
					}
				}
			}
		}
	}

	// 중복제거된 리스트 페이징 결과 리턴
	public List<DocxDto> getAllByDocxorderWithPagination(int page, int size) {
		int startRow = (page - 1) * size + 1;
		int endRow = page * size;
		List<Docx> docxList = dao.findAllByDocxorderWithPagination(startRow, endRow);
		List<DocxDto> docxDtoList = new ArrayList<>();
		for (Docx docx : docxList) {
			docxDtoList.add(new DocxDto(docx.getFormnum(), docx.getWriter(), docx.getSenior(), docx.getStartdt(),
					docx.getEnddt(), docx.getTitle(), docx.getContent(), docx.getNote(), docx.getTaskclasf(),
					docx.getTaskplan(), docx.getTaskprocs(), docx.getTaskprocsres(), docx.getDeptandmeetloc(),
					docx.getDayoffclasf(), docx.getParticipant(), docx.getFormtype(), docx.getAprovdoc(),
					docx.getDocxorder(), docx.getStatus(), docx.getDocxkey(), docx.getOrderloc()));
		}
		 // 현재 페이지에 표시된 문서 수가 size보다 적을 경우 빈 행을 추가하여 페이지 크기를 유지
	
			int currentSize = docxDtoList.size();
	        int emptyRows = size - currentSize;
	        for (int i = 0; i < emptyRows; i++) {
	            // 빈 행을 추가할 때 빈 문자열("") 또는 필요한 경우 null로 초기화된 DTO 객체를 추가할 수 있음
	            docxDtoList.add(new DocxDto(0,new Users(),null,null,null,null,null,null,0,null,null,null,null,null,null,null,0,0,0,0,0));
	        }
		return docxDtoList;
	}

	// 중복 제거 된 리스트의 토탈수 리턴
	public int getTotalCountByDocxorder() {
		return dao.countByDocxorder(0);
	}

	// 멤버리스트 뽑기
	public ArrayList<MembersDto> getMemAll() {
		List<Members> l = mdao.findAll();
		ArrayList<MembersDto> mlist = new ArrayList<MembersDto>();
		for (Members m : l) {
			mlist.add(new MembersDto(m.getUserid(), m.getMemberid(), m.getBirthdt(), m.getEmail(), m.getCpnum(),
					m.getAddress(), m.getMemberimgnm(), m.getHiredt(), m.getLeavedt(), m.getDeptid(), m.getJoblvid(),
					m.getMgrid(), null, null));
		}
		return mlist;
	}

	// 내가 작성한 문서만 출력 , paging
	public List<DocxDto> getUserDocxByPagination(String writer, int page, int size) {
		int startRow = (page - 1) * size + 1;
		int endRow = page * size;
		List<Docx> docxlist = dao.findUserDocxWithPaginationAndDocxOrder(writer, startRow, endRow);
		List<DocxDto> docxDtoList = new ArrayList<>();
		for (Docx docx : docxlist) {
			if (docx.getDocxorder() == 0) {
				docxDtoList.add(new DocxDto(docx.getFormnum(), docx.getWriter(), docx.getSenior(), docx.getStartdt(),
						docx.getEnddt(), docx.getTitle(), docx.getContent(), docx.getNote(), docx.getTaskclasf(),
						docx.getTaskplan(), docx.getTaskprocs(), docx.getTaskprocsres(), docx.getDeptandmeetloc(),
						docx.getDayoffclasf(), docx.getParticipant(), docx.getFormtype(), docx.getAprovdoc(),
						docx.getDocxorder(), docx.getStatus(), docx.getDocxkey(), docx.getOrderloc()));
			}
			int currentSize = docxDtoList.size();
	        int emptyRows = size - currentSize;
	        for (int i = 0; i < emptyRows; i++) {
	            // 빈 행을 추가할 때 빈 문자열("") 또는 필요한 경우 null로 초기화된 DTO 객체를 추가할 수 있음
	            docxDtoList.add(new DocxDto(0,new Users(),null,null,null,null,null,null,0,null,null,null,null,null,null,null,0,0,0,0,0));
	        }

		}
		return docxDtoList;
	}
	
//	//승인된 문서들 페이징처리 출력
//	public List<DocxDto> getApprovedDocx(int page, int size){
//		int startRow = (page - 1) * size+1;
//		int endRow = page * size;
//		List<Docx> docxList = dao.findApprovedDocxWithPagination(startRow, endRow);
//		return convertToDtoList(docxList);
//	}
//	
//	 // 미승인된 문서 목록 반환
//    public List<DocxDto> getUnapprovedDocx(int page, int size) {
//        int startRow = (page - 1) * size + 1;
//        int endRow = page * size;
//        List<Docx> docxList = dao.findUnapprovedDocxWithPagination(startRow, endRow);
//        return convertToDtoList(docxList);
//    }

	public int getUserDocxCount(String writerId) {
		return dao.countUserDocx(writerId);
	}
	
//	 // 승인된 문서 총 개수 반환
//    public int getApprovedDocxCount() {
//        return dao.countApprovedDocx();
//    }
//
//    // 미승인된 문서 총 개수 반환
//    public int getUnapprovedDocxCount() {
//        return dao.countUnapprovedDocx();
//    }

	// 문서 작성자 검색결과를 페이징 처리하여 가져오는 코드
	public List<DocxDto> getByWriterWithPagination(String writerId, int page, int size) {
		int startRow = (page - 1) * size + 1;
		int endRow = page * size;
		List<Docx> docxList = dao.findDistinctByWriter(writerId, startRow, endRow);

		Set<String> seenTitles = new LinkedHashSet<>(); // 중복된 제목을 제거하기 위한 Set
		return docxList.stream().filter(docx -> seenTitles.add(docx.getTitle()))
				.map(docx -> new DocxDto(docx.getFormnum(), docx.getWriter(), null, docx.getStartdt(), docx.getEnddt(),
						docx.getTitle(), docx.getContent(), docx.getNote(), docx.getTaskclasf(), docx.getTaskplan(),
						docx.getTaskprocs(), docx.getTaskprocsres(), docx.getDeptandmeetloc(), docx.getDayoffclasf(),
						docx.getParticipant(), docx.getFormtype(), docx.getAprovdoc(), docx.getDocxorder(),
						docx.getStatus(), docx.getDocxkey(), docx.getOrderloc()))
				.collect(Collectors.toList());
	}

	// 문서 제목 검색 결과를 페이징 처리하여 가져오는 메서드
	public List<DocxDto> getByTitleWithPagination(String title, int page, int size) {
		int startRow = (page - 1) * size + 1;
		int endRow = page * size;
		List<Docx> docxList = dao.findByTitleLike("%" + title + "%", startRow, endRow);

		Set<String> seenTitles = new LinkedHashSet<>(); // 중복된 제목을 제거하기 위한 Set
		return docxList.stream().filter(docx -> seenTitles.add(docx.getTitle()))
				.map(docx -> new DocxDto(docx.getFormnum(), docx.getWriter(), null, docx.getStartdt(), docx.getEnddt(),
						docx.getTitle(), docx.getContent(), docx.getNote(), docx.getTaskclasf(), docx.getTaskplan(),
						docx.getTaskprocs(), docx.getTaskprocsres(), docx.getDeptandmeetloc(), docx.getDayoffclasf(),
						docx.getParticipant(), docx.getFormtype(), docx.getAprovdoc(), docx.getDocxorder(),
						docx.getStatus(), docx.getDocxkey(), docx.getOrderloc()))
				.collect(Collectors.toList());
	}

	public int findByFormtypeDesc(String formtype) {
		List<Docx> l = dao.findByFormtypeOrderByFormnumDesc(formtype);
		ArrayList<DocxDto> list = new ArrayList<DocxDto>();
		for (Docx d : l) {
			list.add(new DocxDto(d.getFormnum(), d.getWriter(), null, d.getStartdt(), d.getEnddt(), d.getTitle(),
					d.getContent(), d.getNote(), d.getTaskclasf(), d.getTaskplan(), d.getTaskprocs(),
					d.getTaskprocsres(), d.getDeptandmeetloc(), d.getDayoffclasf(), d.getParticipant(), d.getFormtype(),
					d.getAprovdoc(), d.getDocxorder(), d.getStatus(), d.getDocxkey(), d.getOrderloc()));
		}
		if (!list.isEmpty()) {
			DocxDto firstDocxDto = list.get(0);
			return firstDocxDto.getDocxkey();
		} else {
			// 리스트가 비어있을 경우에 대한 처리
			return -1; // 예를 들어 -1을 반환하거나 다른 오류 코드를 반환합니다.
		}
	}

	// findByDocxKeyAndFormType(int docxkey , String formtype) 서류종류와 문서유일키를 합쳐서 찾아오기
	public String findByDocxKeyAndFormType(int docxkey, String formtype) {
		List<Docx> l = dao.findByDocxkeyAndFormtype(docxkey, formtype);
		ArrayList<DocxDto> list = new ArrayList<DocxDto>();
		for (Docx d : l) {
			list.add(new DocxDto(d.getFormnum(), d.getWriter(), d.getSenior(), d.getStartdt(), d.getEnddt(),
					d.getTitle(), d.getContent(), d.getNote(), d.getTaskclasf(), d.getTaskplan(), d.getTaskprocs(),
					d.getTaskprocsres(), d.getDeptandmeetloc(), d.getDayoffclasf(), d.getParticipant(), d.getFormtype(),
					d.getAprovdoc(), d.getDocxorder(), d.getStatus(), d.getDocxkey(), d.getOrderloc()));
		}
		if (!list.isEmpty()) {
			DocxDto dto = list.get(0);
			System.out.println(dto.getDocxkey() + "__" + dto.getFormtype());
			return dto.getFormtype() + "-" + dto.getDocxkey();
		} else {
			return "리스트가 비었습니다";
		}

	}

	// senior 출력 문서
	public List<DocxDto> findByDocxKeyTypeSenior(int docxkey, String formtype) {
		List<Docx> l = dao.findByDocxkeyAndFormtype(docxkey, formtype);
		ArrayList<DocxDto> list = new ArrayList<DocxDto>();
		for (Docx d : l) {
			list.add(new DocxDto(d.getFormnum(), d.getWriter(), d.getSenior(), d.getStartdt(), d.getEnddt(),
					d.getTitle(), d.getContent(), d.getNote(), d.getTaskclasf(), d.getTaskplan(), d.getTaskprocs(),
					d.getTaskprocsres(), d.getDeptandmeetloc(), d.getDayoffclasf(), d.getParticipant(), d.getFormtype(),
					d.getAprovdoc(), d.getDocxorder(), d.getStatus(), d.getDocxkey(), d.getOrderloc()));

		}
		return list;
	}

	// 문서 삭제
	public void delDocx(int docxkey) {
		dao.deleteByDocxkey(docxkey);
	}

	public ArrayList<DocxDto> SelectedList(String id) {
		List<Docx> l = dao.findBySenior(id);

		ArrayList<DocxDto> list = new ArrayList<DocxDto>();
		for (Docx d : l) {
			list.add(new DocxDto(d.getFormnum(), d.getWriter(), null, d.getStartdt(), d.getEnddt(), d.getTitle(),
					d.getContent(), d.getNote(), d.getTaskclasf(), d.getTaskplan(), d.getTaskprocs(),
					d.getTaskprocsres(), d.getDeptandmeetloc(), d.getDayoffclasf(), d.getParticipant(), d.getFormtype(),
					d.getAprovdoc(), d.getDocxorder(), d.getStatus(), d.getDocxkey(), d.getOrderloc()));
		}
		return list;
	}
	
	
//	  // Docx 엔티티를 DocxDto로 변환하는 메서드
//    private List<DocxDto> convertToDtoList(List<Docx> docxList) {
//        List<DocxDto> dtoList = new ArrayList<>();
//        for (Docx docx : docxList) {
//            dtoList.add(new DocxDto(docx.getFormnum(), docx.getWriter(), docx.getSenior(), docx.getStartdt(),
//                    docx.getEnddt(), docx.getTitle(), docx.getContent(), docx.getNote(), docx.getTaskclasf(),
//                    docx.getTaskplan(), docx.getTaskprocs(), docx.getTaskprocsres(), docx.getDeptandmeetloc(),
//                    docx.getDayoffclasf(), docx.getParticipant(), docx.getFormtype(), docx.getAprovdoc(),
//                    docx.getDocxorder(), docx.getStatus(), docx.getDocxkey(), docx.getOrderloc()));
//        }
//        return dtoList;
//    }
	

}
