package com.example.demo.oracledb.users;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class UsersAbstractValidator<T> implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object target, Errors errors) {
		try {
			doValidate((T) target, errors);
		} catch (RuntimeException e) {
			// TODO: handle exception
			log.error("중복 검증 에러", e);
			throw e;
		}
	}

	protected abstract void doValidate(final T dto, final Errors errors);
}
