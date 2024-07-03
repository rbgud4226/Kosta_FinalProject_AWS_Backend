package com.example.demo.oracledb.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UsersCheckUsernmValidator extends UsersAbstractValidator<UsersDto> {
	@Autowired
	private UsersDao dao;

	@Override
	protected void doValidate(UsersDto dto, Errors errors) {
		if (dao.findByUsernm(dto.getUsernm()) != null ) {
			errors.rejectValue("usernm", "이름 중복 오류", "이미 사용중인 이름 입니다.");
		}
	}
}
