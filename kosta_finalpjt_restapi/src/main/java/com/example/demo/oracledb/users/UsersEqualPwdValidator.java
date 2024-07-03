package com.example.demo.oracledb.users;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UsersEqualPwdValidator extends UsersAbstractValidator<UsersDto> {
	@Override
	protected void doValidate(UsersDto dto, Errors errors) {
		if (!dto.getConfirm_pwd().equals(dto.getOldpwd())) {
			errors.rejectValue("confirm_pwd", "비밀번호 일치 오류", "비밀번호가 일치하지 않습니다.");
		}
	}
}
