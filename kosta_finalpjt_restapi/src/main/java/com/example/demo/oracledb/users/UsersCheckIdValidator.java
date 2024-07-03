package com.example.demo.oracledb.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UsersCheckIdValidator extends UsersAbstractValidator<UsersDto> {

	@Autowired
	private UsersDao dao;

	@Override
	protected void doValidate(UsersDto dto, Errors errors) {
		if (dao.existsById(dto.getId())) {
			errors.rejectValue("id", "아이디 중복 오류", "이미 사용중인 아이디 입니다.");
		}
	}
}