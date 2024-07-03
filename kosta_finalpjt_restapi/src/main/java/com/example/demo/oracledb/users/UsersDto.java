package com.example.demo.oracledb.users;

import java.util.List;

import com.example.demo.oracledb.chat.RoomUser.RoomUser;
import com.example.demo.oracledb.members.MembersDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class UsersDto {
	
	@NotBlank(message = "계정: 필수 입력 정보입니다.")
	@Pattern(regexp = "^([a-z0-9]){3,30}$", message = "계정: 3~30자의 영문 소문자 및 숫자를 입력해주세요.")
	private String id;
	@NotBlank(message = "이름: 필수 입력 정보입니다.")
//	[ㄱ-ㅎ가-힣a-z0-9-_]
//	@Pattern(regexp = "^[가-힣]{2,10}$", message = "이름: 2~10자의 영문 및 특수문자를 제외한 한글을 입력해주세요.")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z]{2,10}$", message = "이름: 2~10자의 영문 대문자 및 특수문자를 제외한 한글을 입력해주세요.")
	private String usernm;
	
	@NotBlank(message = "비밀번호: 필수 입력 정보입니다.")
//	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 입력해주세요.")
	private String oldpwd;
	
//	@NotBlank(message = "새 비밀번호: 필수 입력 정보입니다.")
//	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 입력해주세요.")
    private String newpwd;
	@NotBlank(message = "비밀번호 확인: 필수 입력 정보입니다.")
	private String confirm_pwd;
	
	private String type;
	private int aprov;
	private MembersDto memberdto;
	private List<RoomUser> roomUsers;
	
//	public Users toEntity() {
//		Users u = Users.builder()
//				.id(id)
//				.usernm(usernm)
//				.pwd(pwd)
//				.type(type)
//				.aprov(aprov)
//				.build();
//		return u;
//	}
}
