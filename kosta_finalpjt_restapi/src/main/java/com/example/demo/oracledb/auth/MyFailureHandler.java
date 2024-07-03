//package com.example.demo.oracledb.auth;
//
//import java.io.IOException;
//
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class MyFailureHandler implements AuthenticationFailureHandler {
//
//	@Override
//	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException exception) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//
////		if (exception instanceof UsernameNotFoundException) {
////			request.setAttribute("loginFailMsg", "존재하지 않는 사용자입니다.");
////
////		} else if (exception instanceof BadCredentialsException) {
////			request.setAttribute("loginFailMsg", "아이디 또는 비밀번호가 맞지 않습니다.");
////
////		} else if (exception instanceof InternalAuthenticationServiceException) {
////			request.setAttribute("loginFailMsg", "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다.");
////
////		} else if (exception instanceof AuthenticationServiceException) {
////			request.setAttribute("loginFailMsg", "인증 요청이 거부되었습니다.");
////
////		}
//////		} else if(exception instanceof LockedException) {
//////			request.setAttribute("loginFailMsg", "잠긴 계정입니다.");
//////			
//////		} else if(exception instanceof DisabledException) {
//////			request.setAttribute("loginFailMsg", "비활성화된 계정입니다.");
//////			
//////		} else if(exception instanceof AccountExpiredException) {
//////			request.setAttribute("loginFailMsg", "만료된 계정입니다.");
//////			
//////		} else if(exception instanceof CredentialsExpiredException) {
//////			request.setAttribute("loginFailMsg", "비밀번호가 만료되었습니다.");
//////		}
////
//////		response.sendRedirect("/loginform");
////
////		// 로그인 페이지로 다시 포워딩
////		RequestDispatcher dispatcher = request.getRequestDispatcher("/loginerror");
////		dispatcher.forward(request, response);
//
//		String errorMessage;
//		if (exception instanceof BadCredentialsException) {
//			errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
//		} else if (exception instanceof InternalAuthenticationServiceException) {
//			errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
//		} else if (exception instanceof UsernameNotFoundException) {
//			errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
//		} else if (exception instanceof AuthenticationServiceException) {
//			errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
//		} else {
//			errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
//		}
//
//		// 로그인 페이지로 다시 포워딩
//		request.setAttribute("errorMessage", errorMessage);
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/loginerror");
//		dispatcher.forward(request, response);
//	}
//}
////super.onAuthenticationFailure(request, response, exception);
