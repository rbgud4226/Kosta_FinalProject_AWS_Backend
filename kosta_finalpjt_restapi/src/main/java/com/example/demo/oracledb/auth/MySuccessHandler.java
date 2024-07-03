//package com.example.demo.oracledb.auth;
//
//import java.io.IOException;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//public class MySuccessHandler implements AuthenticationSuccessHandler {
//	
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		HttpSession session = request.getSession();
//		String loginId = (String) session.getAttribute("loginId");
//		String type = "";
//		if (loginId == null) {
//			session.setAttribute("loginId", authentication.getName());
//
//			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//				type = "admin";
//			} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMP"))) {
//				type = "emp";
//			}
//			session.setAttribute("type", type);
//			response.sendRedirect("/index_" + type);
////			request.getRequestDispatcher("/index_" + type).forward(request, response);
//		} else {
//			String path = "/";
//			response.sendRedirect(path);
//		}
////			request.getRequestDispatcher(path).forward(request, response);
//		System.out.println("MySuccessHandler: " + authentication.getName());
//	}
//
//}
